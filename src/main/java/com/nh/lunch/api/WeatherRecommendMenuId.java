package com.nh.lunch.api;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRecommendMenuId {
	@Column(name = "cache_id")
    private Long cacheId;

    @Column(name = "menu_id")
    private Integer menuId;
}
