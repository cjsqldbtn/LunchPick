package com.nh.lunch.api;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.lunch.chatmessage.BroadSocket;
import com.nh.lunch.menu.Menu;
import com.nh.lunch.menu.MenuRepository;
import com.nh.lunch.place.Place;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import jakarta.transaction.Transactional;

@Service
public class ApiService {
	private final ObjectMapper mapper = new ObjectMapper();
	@Autowired
    private OpenAIClient openAIClient;
	@Autowired
	MenuRepository meRepo;
	@Autowired
	WeatherRecommendCacheRepository wcRepo;
	@Autowired
	WeatherRecommendMenuRepository wmRepo;
	
	public AiMenuDto recommendMenu(AiMenuRequestDto request) throws Exception {
		// 메세지
		List<String> messages = BroadSocket.roomMessages.getOrDefault(request.getRoomKey(), Collections.emptyList());
		// 메뉴들
		List<Menu> menuList = meRepo.findByPlace_TypeAndPriceLessThanEqual(request.getType(), request.getBudget());
		List<AiMenuDto> menus = menuList.stream()
				.map(menu -> new AiMenuDto(menu.getMenuId(), menu.getName()))
				.toList();
		// JSON으로 변환
        String chatJson = mapper.writeValueAsString(messages);
        String menuJson = mapper.writeValueAsString(menus);
        String weatherText = "날씨 조건을 반영하지 않음";
        // 날씨
        if (request.isWeatherOn()) {
            weatherText = """
                    현재 기온: %s℃
                    현재 날씨: %s
                    날씨도 메뉴 선택에 참고해.
                    """.formatted(request.getTemperature(), request.getWeatherIcon());
        }
        // 프롬프트
        String prompt = """
            너는 점심 메뉴 추천 AI야.

            사용자들의 채팅 내용을 가장 중요하게 고려해서
            제공된 메뉴 목록 중 가장 적합한 메뉴 하나를 선택해.

            [날씨]
            %s

            [채팅 내용]
            %s

            [선택 가능한 메뉴]
            %s

            규칙:
            - 반드시 제공된 메뉴 목록 중 하나만 선택한다.
            - 메뉴 목록에 없는 메뉴를 만들지 않는다.
            - menuId와 menuName은 제공된 값을 그대로 사용한다.
            - 날씨는 참고 요소이며 채팅 내용보다 우선하지 않는다.
            - 반드시 JSON 형식으로만 응답한다.

            {
                "menuId": 1,
                "name": "김치찌개"
            }
            """.formatted(weatherText, chatJson, menuJson);

        // OpenAI 호출
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                        .model("gpt-5.2")
                        .addUserMessage(prompt)
                        .build();
        ChatCompletion completion = openAIClient.chat()
                        .completions()
                        .create(params);
        
        // 결과
        String aiResult = completion.choices()
                .get(0)
                .message()
                .content()
                .orElseThrow();
        AiMenuDto recommendation = mapper.readValue(aiResult, AiMenuDto.class);

        // AI가 진짜 존재하는 메뉴를 골랐는지 검증
        boolean exists = menus.stream()
                .anyMatch(menu -> menu.getMenuId().equals(recommendation.getMenuId()));
        if (!exists) {
            throw new IllegalStateException("AI가 존재하지 않는 메뉴를 선택했습니다.");
        }
        
        Menu selectedMenu = meRepo.findById(recommendation.getMenuId()).orElseThrow();

        Place place = selectedMenu.getPlace();

        AiMenuDto result = new AiMenuDto(selectedMenu.getMenuId(), selectedMenu.getName(), place.getPlaceId(), place.getName(), place.getLat(), place.getLng());

        return result;
	}
	
	@Transactional
    public synchronized WeatherRecommendCache refresh(double temperature, String weather) throws Exception {
        /*
         * synchronized 기다리는 동안 다른 요청이
         * 이미 캐시를 생성했을 수도 있으므로 재확인
         */
        WeatherRecommendCache validCache = getValidCache();

        if (validCache != null) { return validCache; }


        // 전체 메뉴 가져오기
        List<Menu> menuList = meRepo.findAll();
        List<AiWeatherRequestDto> candidates = menuList.stream()
                .map(menu -> new AiWeatherRequestDto(menu.getMenuId(), menu.getName(), menu.getPrice(), menu.getPlace().getType()))
                .toList();
        String menuJson = mapper.writeValueAsString(candidates);

        // OpenAI Prompt

        String prompt = """
            너는 점심 메뉴 추천 AI야.

            현재 날씨와 기온을 고려해서
            제공된 메뉴 목록 중 점심으로 적합한 메뉴를 선정해.

            [현재 날씨]
            기온: %s도
            날씨: %s

            [규칙]
            - 반드시 제공된 메뉴만 선택한다.
            - menuId는 제공된 값을 그대로 사용한다.
            - 한성대 메뉴 최소 30개 최대 50개를 선택한다.
            - 신촌 메뉴 최소 30개 최대 50개를 선택한다.
            - 현재 날씨와 어울리는 순서대로 ranking을 부여한다.
            - ranking은 1부터 시작한다.
            - 같은 menuId를 중복 선택하지 않는다.
            - JSON 이외의 설명은 하지 않는다.

            [응답 형식]
            {
              "hansung": [
                {
                  "menuId": 1,
                  "ranking": 1
                }
              ],
              "sinchon": [
                {
                  "menuId": 2,
                  "ranking": 1
                }
              ]
            }

            [선택 가능한 메뉴]
            %s
            """.formatted(
                temperature,
                weather,
                menuJson
            );


        // OpenAI 호출
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model("gpt-5.2")
                .addUserMessage(prompt)
                .build();

        ChatCompletion completion = openAIClient.chat()
                .completions()
                .create(params);


        String result = completion
                .choices()
                .get(0)
                .message()
                .content()
                .orElseThrow();


        AiWeatherResponseDto response = mapper.readValue(result, AiWeatherResponseDto.class);


        // 새로운 Cache 생성
        WeatherRecommendCache cache = new WeatherRecommendCache();
        cache.setTemperature(temperature);
        cache.setWeather(weather);
        cache.setCreatedAt(LocalDateTime.now());
        cache = wcRepo.save(cache);

        // 추천 결과 저장
        saveRecommendMenus(
            cache,
            response.getHansung(),
            "한성대"
        );
        saveRecommendMenus(
            cache,
            response.getSinchon(),
            "신촌"
        );


        return cache;
    }


    private void saveRecommendMenus(WeatherRecommendCache cache, List<AiWeatherDto> recommendations, String type) {
        if (recommendations == null) { return; }

        for (AiWeatherDto aiMenu : recommendations) {
            Menu menu = meRepo.findById(aiMenu.getMenuId()).orElse(null);

            // AI가 이상한 ID 반환했을 경우 무시
            if (menu == null) {
                continue;
            }

            // AI가 장소 타입까지 잘못 선택한 경우 방어
            if (!type.equals(menu.getPlace().getType())) {
                continue;
            }


            WeatherRecommendMenu recommend = new WeatherRecommendMenu();
            WeatherRecommendMenuId id = new WeatherRecommendMenuId(cache.getCacheId(),menu.getMenuId());

            recommend.setId(id);
            recommend.setCache(cache);
            recommend.setMenu(menu);
            recommend.setPlaceType(type);
            recommend.setRanking(aiMenu.getRanking());

            wmRepo.save(recommend);
        }
    }
	
	private WeatherRecommendCache getValidCache() {
        Optional<WeatherRecommendCache> optional = wcRepo.findLatestCache();

        if (optional.isEmpty()) { return null; }

        WeatherRecommendCache cache = optional.get();
        LocalDateTime expireTime = LocalDateTime.now().minusHours(1);
        if (cache.getCreatedAt().isBefore(expireTime)) { return null; }
        return cache;
    }
	
	@Transactional
	public WeatherRecommendCache getOrCreateCache(Double temperature, String weather) throws Exception {
	    WeatherRecommendCache cache = getValidCache();
	    if (cache != null) { return cache; }

	    if (temperature == null || weather == null) {
	        throw new IllegalArgumentException( "날씨 추천 생성에 필요한 날씨 정보가 없습니다.");
	    }

	    return refresh(temperature, weather);
	}
}
