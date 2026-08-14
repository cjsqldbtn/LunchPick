package com.nh.lunch.place;

import java.util.List;

import com.nh.lunch.menu.Menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place {
	@Id
	private Long placeId;
	
	@Column(columnDefinition="VARCHAR2(1000 BYTE)",nullable = false)
	@NotNull
	private String name;
	
	@Column(nullable = false)
	@NotNull
	private Double lat;
	
	@Column(nullable = false)
	@NotNull
	private Double lng;
	
	@Column(columnDefinition="VARCHAR2(15 BYTE)")
	private String phone;
	
	@Column(columnDefinition="VARCHAR2(100 BYTE)")
	private String category;
	
	@Column(columnDefinition="VARCHAR2(2000 BYTE)")
	private String url;
	
	@Column(columnDefinition="VARCHAR2(10 BYTE)",nullable = false)
	@NotNull
	private String type;
	
	@Column(columnDefinition="VARCHAR2(2000 BYTE)")
	private String img;
	
	@OneToMany(mappedBy="place")
	private List<Menu> menu;
}
