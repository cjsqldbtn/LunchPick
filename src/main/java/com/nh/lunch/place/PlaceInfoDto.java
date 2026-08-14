package com.nh.lunch.place;

import java.util.List;
import java.util.stream.Collectors;

import com.nh.lunch.menu.Menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceInfoDto {
	private String img;
	private String placeName;
	private String category;
	private List<String> menu;
	private String phone;
	private String url;
	
	public PlaceInfoDto(Place place) {
		img = place.getImg();
		placeName = place.getName();
		category = place.getCategory();
		menu = place.getMenu().stream()
				.map(Menu::getName)
				.collect(Collectors.toList());
		phone = place.getPhone();
		url = place.getUrl();
	}
}
