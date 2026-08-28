package com.nh.lunch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.nh.lunch.place.Place;
import com.nh.lunch.place.PlaceInfoDto;
import com.nh.lunch.place.PlaceMapDto;
import com.nh.lunch.place.PlaceRepository;
import com.nh.lunch.place.PlaceService;

@SpringBootTest
@ActiveProfiles("test")
class PlaceTests {
	@Autowired
	PlaceRepository pRepo;
	@Autowired
	PlaceService pSvc;
	
	@Test
	void testRopoGetFromPriceAndType() {
		// 1) Given
		int price = 12000;
		String type = "한성대";
		
		// 2) When
		List<PlaceMapDto> list = pRepo.getFromPriceAndType(price,type);
		
		// 3) Then
//		for(int i=0;i<list.size();i++) {
//			System.out.println(list.get(i).getPlaceId());
//		}
	}
	
	@Test
	void testSvcGetPlaceInfo() {
		// 1) Given
		Long placeId = (long)839397094;
		
		// 2) When
		PlaceInfoDto dto = pSvc.getPlaceInfo(placeId);
		
		// 3) Then
		assertNotNull(dto, "부부식당 dto는 null이 아님");
		System.out.println(dto.getPlaceName());
	}
	
	@Test
	void testSvcGetPlacelist() {
		// 1) Given
		int price = 12000;
		String type = "한성대";
		
		// 2) When
		List<PlaceMapDto> list = pSvc.getPlacelist(price, type);
		
		// 3) Then
		assertNotNull(list, "장소리스트는 null이 아님");
//		for(int i=0;i<list.size();i++) {
//			System.out.println(list.get(i).getPlaceId());
//		}
	}
}
