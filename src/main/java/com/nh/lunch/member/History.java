package com.nh.lunch.member;

import com.nh.lunch.menu.Menu;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class History {
//	@Id
//	@ManyToOne
//	@JoinColumn(name="member_id")
//	private Member member;
//	
//	@Id
//	private LocalDateTime finalDate;
	
	@EmbeddedId
	private HistoryId historyId;
	
	@MapsId("memberId")
	@ManyToOne
	@JoinColumn(name="member_id")
	private Member member;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="menu_id", nullable = false)
	private Menu menu;
}
