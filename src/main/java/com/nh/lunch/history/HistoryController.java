package com.nh.lunch.history;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nh.lunch.place.PlaceMapDto;

@RestController
public class HistoryController {
	@Autowired
	HistoryService hSvc;
	
	@PutMapping("/history/{menuId}")
	public void addHistory(@PathVariable("menuId") Integer menuId, Authentication authentication) {
		Integer memberId = (Integer) authentication.getPrincipal();
		
		try {
			hSvc.insertHistory(memberId, menuId);
		} catch (Exception e) {
			e.setStackTrace(null);
		}
	}
	
	@DeleteMapping("/history/delete/{finalDate}")
	public void deleteHistory(Authentication authentication, @PathVariable("finalDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finalDate) {
		Integer memberId = (Integer) authentication.getPrincipal();
		
		try {
			hSvc.deleteHistoryById(memberId, finalDate);
		} catch (Exception e) {
			e.setStackTrace(null);
		}
	}
	
	@GetMapping("/history/list")
	public List<RecentPicksDto> placeList(Authentication authentication) {
		Integer memberId = (Integer) authentication.getPrincipal();
		
		return hSvc.getHistorysByMemberId(memberId);
	}
}
