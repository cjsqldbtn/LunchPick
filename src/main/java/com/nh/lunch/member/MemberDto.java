package com.nh.lunch.member;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
	private Integer memberId;
	private String email;
	private String pw;
	private String passwordKey;
	private LocalDateTime exDate;
	private String chatKey;
	
	public MemberDto(Member m) {
		this.memberId = m.getMemberId();
		this.email = m.getEmail();
		this.pw = m.getPw();
		this.passwordKey = m.getPasswordKey();
		this.exDate = m.getExDate();
		this.chatKey = m.getChatKey();
	}	
}
