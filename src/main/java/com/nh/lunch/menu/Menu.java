package com.nh.lunch.menu;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.nh.lunch.member.History;
import com.nh.lunch.place.Place;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Menu {
	@Id
	private Integer menuId;
	
	@ManyToOne
	@JoinColumn(name="place_id")
	private Place place;
	
	@Column(columnDefinition="VARCHAR2(1000 BYTE)", nullable = false)
	@NotNull
	private String name;
	
	@Column(nullable = false)
	@NotNull
	private Integer price;
	
	@Column(nullable = false)
	@NotNull
	@ColumnDefault("0")
	private Integer count;
	
	
	@OneToMany(mappedBy="menu")
	private List<History> history;
}
