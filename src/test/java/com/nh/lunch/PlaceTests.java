package com.nh.lunch;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nh.lunch.place.Place;
import com.nh.lunch.place.PlaceMapDto;
import com.nh.lunch.place.PlaceRepository;

@SpringBootTest
class PlaceTests {
	@Autowired
	PlaceRepository pRepo;
	
	@Test
	void testRopogetFromPriceAndType() {
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
	
	
}
