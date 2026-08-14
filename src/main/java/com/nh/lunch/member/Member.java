package com.nh.lunch.member;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqGen1")
	@SequenceGenerator(name="seqGen1", sequenceName = "member_seq", allocationSize=1)
	private Integer memberId;
	
	@Column(columnDefinition="VARCHAR2(1000 BYTE)")
	private String email;
	
	private String pw;
	
	@Column(columnDefinition="CHAR(6 BYTE)")
	private String passwordKey;
	
	@Column(columnDefinition="CHAR(6 BYTE)")
	private String chatKey;
	
	private LocalDateTime exDate;
}
