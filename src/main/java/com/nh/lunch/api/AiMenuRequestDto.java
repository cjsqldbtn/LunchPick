package com.nh.lunch.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiMenuRequestDto {
	private String roomKey;
	private String type;
	private int budget;
	private boolean weatherOn;
	private Double temperature;
    private String weatherIcon;
}
