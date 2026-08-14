package com.nh.lunch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nh.lunch.place.PlaceRepository;

@SpringBootTest
class PlaceTests {
	@Autowired
	PlaceRepository pRepo;

	@Test
	void contextLoads() {
	}

}
