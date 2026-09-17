package com.nh.lunch.history;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
