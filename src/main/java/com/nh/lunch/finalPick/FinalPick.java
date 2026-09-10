package com.nh.lunch.finalPick;

import com.nh.lunch.history.HistoryId;
import com.nh.lunch.member.Member;
import com.nh.lunch.menu.Menu;
import com.nh.lunch.place.Place;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FinalPick {
	@EmbeddedId
	private FinalPickId finalPickId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="place_id", nullable = false)
	private Place place;
}
