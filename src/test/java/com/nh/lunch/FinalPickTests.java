package com.nh.lunch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.nh.lunch.finalPick.FinalPickRepository;
import com.nh.lunch.history.HistoryRepository;
import com.nh.lunch.history.HistoryService;

@SpringBootTest
@ActiveProfiles("test")
class FinalPickTests {
	@Autowired
	FinalPickRepository fRepo;
	
	@Test
	void testFindTodayPickedPlaceIds() {
		// 1) Given
		String userSession = "ADA2F63FBE04010AD56265C50C072ED9";
		LocalDate today = LocalDate.now();
		
		// 2) When
		List<Long> list = fRepo.findTodayPickedPlaceIds(userSession, today);
		
		// 3) Then
		assertNotNull(list);
//		for(int i=0;i<list.size();i++) {
//			System.out.println("placeId : " + list.get(i));
//		}
	}
}
