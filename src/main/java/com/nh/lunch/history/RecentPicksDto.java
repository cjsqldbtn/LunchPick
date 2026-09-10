package com.nh.lunch.history;

import java.time.LocalDateTime;

import com.nh.lunch.menu.MenuDto;
import com.nh.lunch.place.PlaceMapDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentPicksDto {
	private LocalDateTime finalDate;
	private MenuDto menu;
	private PlaceMapDto place;
	private String placeName;
	private String placeCategory;
	private String menuName;
	private String price;
}
