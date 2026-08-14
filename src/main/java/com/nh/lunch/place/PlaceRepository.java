package com.nh.lunch.place;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {
	@Query(
			value = "SELECT p.place_id, p.lat, p.lng, m.menu_id"
					+ " FROM place p INNER JOIN menu m ON p.place_id = m.place_id"
					+ " WHERE m.price <= :price and p.type = :type",
			nativeQuery = true
			)
	List<PlaceMapDto> getFromPriceAndType(@Param("price") int price, @Param("type") String type);
}
