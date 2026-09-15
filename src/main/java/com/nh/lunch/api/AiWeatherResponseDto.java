package com.nh.lunch.api;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiWeatherResponseDto {
	private List<AiWeatherDto> hansung;
    private List<AiWeatherDto> sinchon;
}
