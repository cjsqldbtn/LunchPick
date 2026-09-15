package com.nh.lunch.place;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {
	@Query(
			value = "SELECT DISTINCT p.place_id, p.lat, p.lng"
					+ " FROM place p INNER JOIN menu m ON p.place_id = m.place_id"
					+ " WHERE m.price <= :price and p.type = :type",
			nativeQuery = true
			)
	List<PlaceMapDto> getFromPriceAndType(@Param("price") int price, @Param("type") String type);
	@Query(
		    value = """
		        SELECT DISTINCT
		            p.place_id AS placeId,
		            p.lat AS lat,
		            p.lng AS lng
		        FROM place p
		        INNER JOIN menu m
		            ON p.place_id = m.place_id
		        INNER JOIN weather_recommend_menu wrm
		            ON m.menu_id = wrm.menu_id
		        WHERE m.price <= :price
		          AND p.type = :type
		          AND wrm.cache_id = :cacheId
		        """,
		    nativeQuery = true
		)
	List<PlaceMapDto> getFromWeatherAndPriceAndType(@Param("price") int price, @Param("type") String type, @Param("cacheId") Long cacheId);
}
