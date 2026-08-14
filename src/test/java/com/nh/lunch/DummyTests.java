package com.nh.lunch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nh.lunch.place.Place;
import com.nh.lunch.place.PlaceRepository;

@SpringBootTest
class DummyTests {
	@Autowired
	PlaceRepository pRepo;

	@Test
	void testPlaceInsertDummy() {
		Place p1 = new Place();
		p1.setPlaceId((long) 839397094);
		p1.setName("부부식당");
		p1.setCategory("분식");
		p1.setLat(37.58021304);
		p1.setLng(127.004302);
		p1.setPhone("02-765-6056");
		p1.setUrl("http://place.map.kakao.com/839397094");
		p1.setType("한성대");
		p1.setImg("https://img1.kakaocdn.net/cthumb/local/C800x800.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F73e062869cc1ab53024fbd8c48b1ec278ce95b60%3Foriginal");
		
		pRepo.save(p1);
		
		Place p2 = new Place();
		p2.setPlaceId((long) 1438226039);
		p2.setName("소바의온도 본점");
		p2.setCategory("일식");
		p2.setLat(37.58212766);
		p2.setLng(127.0039546);
		p2.setUrl("http://place.map.kakao.com/1438226039");
		p2.setType("한성대");
		p2.setImg("https://img1.kakaocdn.net/cthumb/local/C800x800.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F9a7c93f7289f4dc7ad495d553599f5807dcba82d%3Foriginal");
		
		pRepo.save(p2);
		
		Place p3 = new Place();
		p3.setPlaceId((long) 2014194074);
		p3.setName("삼미탄탄면");
		p3.setCategory("중식");
		p3.setLat(37.55758354);
		p3.setLng(126.9377868);
		p3.setUrl("http://place.map.kakao.com/2014194074");
		p3.setType("신촌");
		p3.setImg("https://img1.kakaocdn.net/cthumb/local/C800x800.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocal%2FkakaomapPhoto%2Freview%2F495353072575d0099ce854583eefa73e3f3bd241%3Foriginal");
		
		pRepo.save(p3);
	}

}
