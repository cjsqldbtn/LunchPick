package com.nh.lunch.error;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorReportService {
	@Autowired
	ErrorReportRepository erRepo;
	
	/**
	 * 오류 제보 페이지에 삽입.
	 * @param img : 삽입할 이미지.
	 * @param content : 삽입할 내용.
	 */
	public void insertErrorReport(String img,String content) {
		ErrorReport er = new ErrorReport();
		er.setImg(img);
		er.setFinalDate(LocalDateTime.now());
		er.setContent(content);
		erRepo.save(er);
	}
}
