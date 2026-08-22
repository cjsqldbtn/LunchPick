package com.nh.lunch.place;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceController {
	@Autowired
	PlaceService pSvc;
	
	@GetMapping("/place/list")
	public List<PlaceMapDto> placeList(@RequestParam(value="type", required = false) String type, @RequestParam(value="price", required = false) Integer price) {
		if(type==null) type="한성대";
		if(price==null) price=70000;
		return pSvc.getPlacelist(price, type);
	}
}
