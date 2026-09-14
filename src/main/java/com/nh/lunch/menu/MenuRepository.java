package com.nh.lunch.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	List<Menu> findByPlace_Type(String type);
	List<Menu> findByPlace_TypeAndPriceLessThanEqual(String type, int price);
}
