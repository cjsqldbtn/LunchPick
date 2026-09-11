package com.nh.lunch.finalPick;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nh.lunch.place.PlacePickCountDto;

public interface FinalPickRepository extends JpaRepository<FinalPick, FinalPickId> {
	// 오늘 현재 세션에서 이미 선택된 장소
	@Query("""
		    SELECT f.place.placeId
		    FROM FinalPick f
		    WHERE f.finalPickId.userSession = :userSession
		      AND f.finalPickId.pickDate = :today
		""")
	List<Long> findTodayPickedPlaceIds(@Param("userSession") String userSession, @Param("today") LocalDate today);
	
    // 최근 30일 동안 장소별 선택 횟수
    @Query("""
	        SELECT new com.nh.lunch.place.PlacePickCountDto(f.place.placeId, COUNT(f))
	        FROM FinalPick f
	        WHERE f.finalPickId.pickDate >= :from
	          AND f.place.placeId IN :placeIds
	        GROUP BY f.place.placeId
	    """)
    List<PlacePickCountDto> countRecentPickByPlaceIds(@Param("placeIds") List<Long> placeIds, @Param("from") LocalDate from);
}
