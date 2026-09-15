package com.nh.lunch.api;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRecommendMenuRepository extends JpaRepository<WeatherRecommendMenu, WeatherRecommendMenuId>{
	List<WeatherRecommendMenu> findByCache_CacheIdAndPlaceTypeOrderByRankingAsc(Long cacheId, String placeType);
}
