package com.nh.lunch.finalPick;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.lunch.place.Place;
import com.nh.lunch.place.PlaceRepository;

import jakarta.transaction.Transactional;

@Service
public class FinalPickService {
	@Autowired
	private FinalPickRepository fRepo;
	@Autowired
	private PlaceRepository pRepo;
	
	@Transactional
	public boolean insertFinalPick(String userSession, Long placeId) {
		Optional<Place> op = pRepo.findById(placeId);
		if(op.isEmpty()) {
			// place가 있지 않으면
			return false;
		}
		
		FinalPick fp = new FinalPick();
		fp.setFinalPickId(new FinalPickId(userSession, LocalDate.now()));
		fp.setPlace(op.get());
		fRepo.save(fp);
		return true;
	}
	
}
