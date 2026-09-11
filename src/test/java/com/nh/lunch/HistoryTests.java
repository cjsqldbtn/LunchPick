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

import com.nh.lunch.history.HistoryRepository;
import com.nh.lunch.history.HistoryService;

@SpringBootTest
@ActiveProfiles("test")
class HistoryTests {
	@Autowired
	HistoryRepository hRepo;
	@Autowired
	HistoryService hSvc;

	// 히스토리 삽입 
	@Test
	@Transactional
	void testInsertHistory() {
		// 1) Given
		int memberId = 1;
		int menuId = 2;
		
		// 2) When
		boolean result = hSvc.insertHistory(memberId, menuId);
		
		// 3) Then : false이면 메뉴 ID 비존재.
		assertTrue(result, "메뉴 ID나 멤버 ID가 잘못됐습니다.");
//		System.out.println(result);
	}
	
	// 최근 히스토리 조회
	@Test
	void testFindRecentPlaceIds() {
		// 1) Given
		Integer memberId = 1;
		LocalDateTime from = LocalDate.now().minusDays(2).atStartOfDay();
		
		// 2) When
		List<Integer> list = hRepo.findRecentPlaceIds(memberId, from);
		
		// 3) Then
		assertNotNull(list);
//		for(int i=0;i<list.size();i++) {
//			System.out.println("placeId: " + list.get(i));
//		}
	}
	
	/*
	 * // 해당 멤버가 선택한 메뉴명들 조회.
	 * 
	 * @Test void testGetHistorysByMemberId() { // 1) Given int memberId = 2;
	 * 
	 * // 2) When List<String> result = hSvc.getHistorysByMemberId(memberId);
	 * 
	 * // 3) Then assertNotNull(result, "memberId가 잘못됐습니다."); //
	 * System.out.println(result);
	 * 
	 * }
	 */

}
