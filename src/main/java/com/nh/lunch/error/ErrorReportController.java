package com.nh.lunch.error;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ErrorReportController {
	@Autowired
	ErrorReportService eSvc;
	
	@PostMapping("/report")
	public ResponseEntity<Void> insertErrorReport(
	        @RequestParam(value = "content", required = false) String content,
	        @RequestParam(value = "image", required = false) MultipartFile image) {

	    // 둘 다 없는 경우
	    if ((content == null || content.trim().isEmpty())
	            && (image == null || image.isEmpty())) {
	        return ResponseEntity.badRequest().build();
	    }

	    String img = null;
	    try {
	        if (image != null && !image.isEmpty()) {
	            // 저장 폴더
	            String uploadDir = "C:/upload/report/";
	            File folder = new File(uploadDir);

	            if (!folder.exists()) { // 경로가 없다면 상위 폴더까지 생성
	                folder.mkdirs();
	            }

	            // 파일명 중복 방지
	            String originalName = image.getOriginalFilename();
	            String saveName = UUID.randomUUID() + "_" + originalName;

	            // 실제 파일 저장
	            File saveFile = new File(uploadDir, saveName);
	            image.transferTo(saveFile);

	            // DB에는 파일명만 저장
	            img = saveName;
	        }

	        eSvc.insertErrorReport(img, content);
	        return ResponseEntity.ok().build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .internalServerError()
	                .build();
	    }
	}
}
