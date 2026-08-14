package com.nh.lunch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.nh.lunch.member.HistoryService;

@SpringBootTest
class HistoryTests {
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
	
	// 해당 멤버가 선택한 메뉴명들 조회.
	@Test
	void testGetHistorysByMemberId() {
		// 1) Given
		int memberId = 2;
		
		// 2) When
		List<String> result = hSvc.getHistorysByMemberId(memberId);
		
		// 3) Then
		assertNotNull(result, "memberId가 잘못됐습니다.");
//		System.out.println(result);
		
	}

}
