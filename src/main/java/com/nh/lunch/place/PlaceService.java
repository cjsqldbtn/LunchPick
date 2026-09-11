package com.nh.lunch.place;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.lunch.finalPick.FinalPick;
import com.nh.lunch.finalPick.FinalPickId;
import com.nh.lunch.finalPick.FinalPickRepository;
import com.nh.lunch.history.HistoryRepository;
import com.nh.lunch.member.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaceService {
	@Autowired
	MemberRepository mRepo;
	@Autowired
	PlaceRepository pRepo;
	@Autowired
	HistoryRepository hRepo;
	@Autowired
	FinalPickRepository fRepo;
	
	/**
	 * 장소 맵 정보 조회
	 * @param price 가격
	 * @param type 장소
	 * @return PlaceMapDto
	 */
	public List<PlaceMapDto> getPlacelist(int price, String type) {
		return pRepo.getFromPriceAndType(price, type);
	}
	
	/**
	 * 장소 정보 조회
	 * @param placeId
	 * @return PlaceInfoDto
	 */
	@Transactional
	public PlaceInfoDto getPlaceInfo(Long placeId) {
		if(placeId==null) return null;
		
		Optional<Place> op = pRepo.findById(placeId);
		if(op.isEmpty()) return null;
		
		return new PlaceInfoDto(op.get());
	}
	
	/**
	 * 장소 룰렛
	 * @param placeIds 룰렛 돌릴 장소 아이디 리스트
	 * @param memberId 비로그인시 null
	 * @param userSession
	 * @return
	 */
	@Transactional
    public Long selectPlace(List<Long> placeIds, Integer memberId, String userSession) {
        // 로그인 상태라면 3일 동안 먹은 장소 제외
        Set<Integer> recentPlaceIds = new HashSet<>();
        if (memberId != null) {
            LocalDateTime from = LocalDate.now().minusDays(2).atStartOfDay();
            recentPlaceIds.addAll(hRepo.findRecentPlaceIds(memberId,from));
        }


        // 오늘 같은 세션에서 이미 룰렛으로 나온 장소 제외
        LocalDate today = LocalDate.now();
        Set<Long> todayPickedPlaceIds = new HashSet<>(fRepo.findTodayPickedPlaceIds(userSession, today));


        // 제외 대상 제거
        List<Long> availablePlaceIds = placeIds.stream()
        		.filter(placeId -> !recentPlaceIds.contains(placeId))
                .filter(placeId -> !todayPickedPlaceIds.contains(placeId))
                .toList();
        
        // 무한 루프 방지 추천할 수 없음 에러
        if (availablePlaceIds.isEmpty()) { throw new IllegalStateException("선택 가능한 장소가 없습니다."); }


        // 사용자 선택 결과 반영(30일 선택 결과들로 가중치 부여)
        LocalDate countFrom = today.minusDays(30);
        List<PlacePickCountDto> pickCountList = fRepo.countRecentPickByPlaceIds(availablePlaceIds, countFrom);

        // 장소별 선택 횟수 Map
        Map<Long, Long> pickCounts = new HashMap<>();

        for (PlacePickCountDto dto : pickCountList) {
            pickCounts.put(dto.getPlaceId(), dto.getPickCount());
        }


        // 가중치 랜덤 선택
        Long selectedPlaceId = weightedRandom(availablePlaceIds, pickCounts);


        // 최종 결과
        Optional<Place> op = pRepo.findById(selectedPlaceId);
        if(op.isEmpty()) return null;
        Place selectedPlace = op.get();
        
        FinalPickId finalPickId = new FinalPickId();
        finalPickId.setUserSession(userSession);
        finalPickId.setPickDate(today);

        FinalPick finalPick = new FinalPick();
        finalPick.setFinalPickId(finalPickId);
        finalPick.setPlace(selectedPlace);

        fRepo.save(finalPick);

        return selectedPlaceId;
    }


    /*
     * 선택 횟수가 많은 장소일수록
     * 조금 더 높은 확률을 갖도록 함
     *
     * count = 0 → weight 1
     * count = 1 → weight 2
     * count = 4 → weight 3
     * count = 9 → weight 4
     */
    private Long weightedRandom(List<Long> placeIds, Map<Long, Long> pickCounts) {
        List<Long> weightedPlaces = new ArrayList<>();

        for (Long placeId : placeIds) {
            long count = pickCounts.getOrDefault(placeId, 0L);
            int weight = 1 + (int) Math.sqrt(count);
            for (int i = 0; i < weight; i++) {
                weightedPlaces.add(placeId);
            }
        }

        return weightedPlaces.get(ThreadLocalRandom.current().nextInt(weightedPlaces.size()));
    }
}
