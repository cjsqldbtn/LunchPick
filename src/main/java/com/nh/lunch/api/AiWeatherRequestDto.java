package com.nh.lunch.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiWeatherRequestDto {
	private Integer menuId;
    private String name;
    private Integer price;
    private String placeType;
}
