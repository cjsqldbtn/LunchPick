package com.nh.lunch.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Menu {
	@Id
	private int menu;
	
	private int place_id;
	
	@Column(columnDefinition="VARCHAR2(1000 BYTE)")
	private String name;
	
	private int price;
	
	private int count;
}
