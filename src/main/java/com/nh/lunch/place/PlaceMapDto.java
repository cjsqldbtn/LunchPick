package com.nh.lunch.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceMapDto {
	private Long placeId;
	private Integer memuId;
	private Double lat;
	private Double lng;
	
	public PlaceMapDto(Long placeId, Double lat, Double lng, Integer menuId) {
		this.placeId = placeId;
		this.lat = lat;
		this.lng = lng;
		this.memuId = memuId;
	}
}
