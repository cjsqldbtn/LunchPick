package com.nh.lunch.member;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class History {
	@Id
	private int member_id;
	
	private LocalDateTime final_date;
	
	private int menu_id;
}
