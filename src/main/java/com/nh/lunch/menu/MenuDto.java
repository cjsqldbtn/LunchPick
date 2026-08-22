package com.nh.lunch.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuDto {
	private Integer menuId;
	private Long placeId;
	private String name;
	private Integer price;
	private Integer count;
	
	public MenuDto(Menu menu) {
		menuId = menu.getMenuId();
		placeId = menu.getPlace().getPlaceId();
	}
}
