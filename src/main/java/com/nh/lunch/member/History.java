package com.nh.lunch.member;

import java.time.LocalDateTime;

import com.nh.lunch.menu.Menu;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class History {
	@Id
	private Integer memberId;

	@ManyToOne
	@JoinColumn(name="member_id")
	private Member member;
	
	@Id
	private LocalDateTime finalDate;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="menu_id", nullable = false)
	private Menu menu;
}
