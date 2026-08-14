package com.nh.lunch.error;

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
public class ErrorReport {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqGen1")
	@SequenceGenerator(name="seqGen1", sequenceName = "member_seq", allocationSize=1)
	private Integer errorId;
	
	@Column(columnDefinition="VARCHAR2(2000 BYTE)")
	private String email;
	
	@Column(columnDefinition="VARCHAR2(4000 BYTE)")
	private String content;
	
	private LocalDateTime finalDate;
}
