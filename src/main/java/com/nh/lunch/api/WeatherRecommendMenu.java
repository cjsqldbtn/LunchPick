package com.nh.lunch.api;

import com.nh.lunch.menu.Menu;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WeatherRecommendMenu {
	@EmbeddedId
    private WeatherRecommendMenuId id;

    @MapsId("cacheId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cache_id")
    private WeatherRecommendCache cache;

    @MapsId("menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "place_type", nullable = false)
    private String placeType;

    @Column(name = "ranking", nullable = false)
    private Integer ranking;
}
