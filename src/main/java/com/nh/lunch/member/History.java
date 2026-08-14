package com.nh.lunch.member;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class History {
	@Id
	private Integer member_id;
	
	private LocalDateTime final_date;
	
	private int menu_id;
}
