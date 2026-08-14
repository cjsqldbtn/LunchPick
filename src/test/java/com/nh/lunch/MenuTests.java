package com.nh.lunch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.nh.lunch.menu.MenuDto;
import com.nh.lunch.menu.MenuRecommendInfoDto;
import com.nh.lunch.menu.MenuService;

@SpringBootTest
class MenuTests {
	@Autowired
	MenuService meSvc;

	@Test
	void testGetMenu() {
		// 1) Given
		Integer menuId = 10;
		
		// 2) When
		MenuDto dto = meSvc.getMenu(menuId);
		
		// 3) Then
		assertNotNull(dto, "10번 menu는 null이 아님");
		//System.out.println(dto.getName());
	}
	
	@Test
	@Transactional
	void testGetMenuRecommendInfo() {
		// 1) Given
		Integer menuId = 10;
		Integer memberId = 1;
		
		// 2) When
		MenuRecommendInfoDto dto = meSvc.getMenuRecommendInfo(menuId, memberId);
		
		// 3) Then
		assertNotNull(dto, "10번 menu는 추천 정보는 null이 아님");
	}
}
