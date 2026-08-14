package com.nh.lunch.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

	@Query(
			value="SELECT sum(m.count) FROM menu m",
			nativeQuery = true
	)
	int totalMenuCnt();
}
