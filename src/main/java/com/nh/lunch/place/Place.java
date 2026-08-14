package com.nh.lunch.place;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place {
	@Id
	private int place_id;
	
	@Column(columnDefinition="VARCHAR2(1000 BYTE)")
	private String name;
	
	private double lat;
	
	private double lng;
	
	@Column(columnDefinition="VARCHAR2(15 BYTE)")
	private String phone;
	
	@Column(columnDefinition="VARCHAR2(100 BYTE)")
	private String category;
	
	@Column(columnDefinition="VARCHAR2(2000 BYTE)")
	private String url;
	
	@Column(columnDefinition="VARCHAR2(10 BYTE)")
	private String type;
	
	@Column(columnDefinition="VARCHAR2(2000 BYTE)")
	private String img;
}
