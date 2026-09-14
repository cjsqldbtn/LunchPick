package com.nh.lunch.api;

import java.util.Collections;
import java.util.List;

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

@Service
public class ApiService {
	private final ObjectMapper mapper = new ObjectMapper();
	@Autowired
    private OpenAIClient openAIClient;
	@Autowired
	MenuRepository meRepo;
	
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
}
