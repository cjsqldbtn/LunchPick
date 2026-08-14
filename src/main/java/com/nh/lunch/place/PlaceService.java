package com.nh.lunch.place;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.lunch.member.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class PlaceService {
	@Autowired
	MemberRepository mRepo;
	@Autowired
	PlaceRepository pRepo;
	
	/**
	 * 장소 맵 정보 조회
	 * @param price 가격
	 * @param type 장소
	 * @return PlaceMapDto
	 */
	public List<PlaceMapDto> getPlacelist(int price, String type) {
		return pRepo.getFromPriceAndType(price, type);
	}
	
	/**
	 * 장소 정보 조회
	 * @param placeId
	 * @return PlaceInfoDto
	 */
	@Transactional
	public PlaceInfoDto getPlaceInfo(Long placeId) {
		if(placeId==null) return null;
		
		Optional<Place> op = pRepo.findById(placeId);
		if(op.isEmpty()) return null;
		
		return new PlaceInfoDto(op.get());
	}
}
