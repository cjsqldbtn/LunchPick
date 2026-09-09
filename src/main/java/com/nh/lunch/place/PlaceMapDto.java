package com.nh.lunch.place;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceMapDto {
	private Long placeId;
	private Double lat;
	private Double lng;
	
	public PlaceMapDto(Long placeId, Double lat, Double lng) {
		this.placeId = placeId;
		this.lat = lat;
		this.lng = lng;
	}
}
