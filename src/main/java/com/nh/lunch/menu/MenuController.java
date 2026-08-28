package com.nh.lunch.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
	@Autowired
	MenuService meSvc;
	
	@PutMapping("/menu/{menuId}")
	public void menuUpdate(@PathVariable("menuId") Integer menuId) {
		//System.out.println(menuId + "요청 들어옴");
		meSvc.addCnt(menuId);
	}
}
