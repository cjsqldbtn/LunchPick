package com.nh.lunch.api;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeatherRecommendCache {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_cache_seq")
    @SequenceGenerator(name = "weather_cache_seq", sequenceName = "weather_recommend_cache_seq", allocationSize = 1)
    @Column(name = "cache_id")
    private Long cacheId;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "weather")
    private String weather;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
