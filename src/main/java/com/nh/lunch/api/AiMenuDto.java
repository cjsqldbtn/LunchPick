package com.nh.lunch.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiMenuDto {
	private Integer menuId;
	private String name;
	
	private Long placeId;
    private String placeName;
    private Double lat;
    private Double lng;
    
    public AiMenuDto (Integer menuId, String name) {
    	this.menuId = menuId;
    	this.name = name;
    }
}
