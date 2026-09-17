package com.nh.lunch.finalPick;

import java.io.Serializable;
import java.time.LocalDate;

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
public class FinalPickId implements Serializable {
	@Column(name="user_session")
	private String userSession;
	
	@Column(name="pick_date")
	private LocalDate pickDate;
}
