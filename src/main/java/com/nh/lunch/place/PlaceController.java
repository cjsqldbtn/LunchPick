package com.nh.lunch.place;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nh.lunch.finalPick.FinalPickService;

import jakarta.servlet.http.HttpSession;

@RestController
public class PlaceController {
	@Autowired
	PlaceService pSvc;
	@Autowired
	FinalPickService fSvc;
	
//	@GetMapping("/test")
//	public String test(HttpSession session) {
//	    System.out.println("sessionId = " + session.getId());
//	    return session.getId();
//	}
	
	@GetMapping("/place/list")
	public List<PlaceMapDto> placeList(@RequestParam(value="type", required = false) String type, @RequestParam(value="price", required = false) Integer price) {
		if(type==null) type="한성대";
		return pSvc.getPlacelist(price, type);
	}
	
	@GetMapping("/place/{placeId}")
	public PlaceInfoDto placeInfo(@PathVariable("placeId") Long placeId, HttpSession session) {
		fSvc.insertFinalPick(session.getId(), placeId);
		return pSvc.getPlaceInfo(placeId);
	}
	
	@PostMapping("/roulette")
	public Long roulette(Authentication authentication, @RequestBody List<Long> placeIds, HttpSession session) {
	    Integer memberId = null;
	    if (authentication != null && authentication.isAuthenticated()) {
	        memberId = (Integer) authentication.getPrincipal();
	    }

	    return pSvc.selectPlace(placeIds, memberId, session.getId());
	}
}
