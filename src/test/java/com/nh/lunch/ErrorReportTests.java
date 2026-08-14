package com.nh.lunch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nh.lunch.error.ErrorReportService;

import jakarta.transaction.Transactional;

@SpringBootTest
class ErrorReportTests {
	@Autowired
	ErrorReportService erSvc;

	// 오류 제보 삽입.
	@Test
	@Transactional
	void testInsertErrorReport() {
		// 1) Given
		String img="이미지.png";
		String content="테스트.";
		
		// 2) When
		erSvc.insertErrorReport(img, content);
		
		
		// 3) Then : 오류가 없으면 성공!
	}

}
