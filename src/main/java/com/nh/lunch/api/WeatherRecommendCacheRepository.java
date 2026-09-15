package com.nh.lunch.api;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeatherRecommendCacheRepository extends JpaRepository<WeatherRecommendCache, Long> {
	 @Query(
		        value = """
		            SELECT *
		            FROM (
		                SELECT *
		                FROM weather_recommend_cache
		                ORDER BY created_at DESC
		            )
		            WHERE ROWNUM = 1
		            """,
		        nativeQuery = true
		    )
    Optional<WeatherRecommendCache> findLatestCache();
}
