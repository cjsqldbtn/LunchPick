package com.nh.lunch.menu;

import java.util.List;
import java.util.stream.Collectors;

import com.nh.lunch.place.Place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRecommendInfoDto {
	private String img;
	private String placeName;
	private String category;
	private String selectedMenu;
	private List<String> menu;
	private String phone;
	private String url;
	
	/**
	 * MenuRecommendInfoDto 생성자
	 * @param place
	 * @param selectedMenu 선택된 메뉴 이름
	 */
	public MenuRecommendInfoDto(Place place, String selectedMenu) {
		img = place.getImg();
		placeName = place.getName();
		category = place.getCategory();
		this.selectedMenu = selectedMenu;
		
		// 골라진 메뉴 제외 List에 담기
		menu = place.getMenu().stream()
				.map(Menu::getName)
				.collect(Collectors.toList());
		menu.remove(selectedMenu);
		
		phone = place.getPhone();
		url = place.getUrl();
	}
}
