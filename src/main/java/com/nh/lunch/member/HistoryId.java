package com.nh.lunch.member;

import java.io.Serializable;
import java.time.LocalDateTime;

import groovy.transform.EqualsAndHashCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class HistoryId implements Serializable {
	@Column(name="member_id")
	private Integer memberId;
	
	@Column(name="final_date")
	private LocalDateTime finalDate;
}
