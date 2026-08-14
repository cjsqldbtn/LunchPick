package com.nh.lunch.member;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
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
	
	@Column(columnDefinition="VARCHAR2(1000 BYTE)", nullable = false, unique = true)
	@NotNull
	private String email;
	
	@Column(nullable = false)
	@NotNull
	private String pw;
	
	@Column(columnDefinition="CHAR(6 BYTE)")
	private String passwordKey;
	
	@Column(columnDefinition="CHAR(6 BYTE)")
	private String chatKey;
	
	private LocalDateTime exDate;
	
	
	@OneToMany(mappedBy="member")
	private List<History> history;
}
