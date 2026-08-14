package com.nh.lunch.member;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryDto {
	private int memberId;
	private LocalDateTime finalDate;
	private int menuId;
	
	public HistoryDto(History h) {
		this.memberId = h.getHistoryId().getMemberId();
		this.finalDate = h.getHistoryId().getFinalDate();
		this.menuId = h.getMenu().getMenuId();
	}
	
}
