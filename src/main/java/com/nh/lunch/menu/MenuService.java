package com.nh.lunch.menu;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.lunch.member.History;
import com.nh.lunch.member.HistoryId;
import com.nh.lunch.member.HistoryRepository;
import com.nh.lunch.member.Member;
import com.nh.lunch.member.MemberRepository;
import com.nh.lunch.place.PlaceRepository;

import jakarta.transaction.Transactional;

@Service
public class MenuService {
	@Autowired
	MenuRepository meRepo;
	@Autowired
	PlaceRepository pRepo;
	@Autowired
	HistoryRepository hRepo;
	@Autowired
	MemberRepository mRepo;
	
	/**
	 * menu 정보 가져오기
	 * @param menuId
	 * @return MenuDto
	 */
	public MenuDto getMenu(Integer menuId) {
		if(menuId==null) return null;
		
		Optional<Menu> ob = meRepo.findById(menuId);
		if(ob.isEmpty()) return null;
		
		return new MenuDto(ob.get());
	}
	
	/**
	 * 선택된 메뉴 팝업 정보 조회(메뉴 cnt 증가 및 히스토리 남기기 포함)
	 * @param menuId
	 * @param memberId
	 * @return
	 */
	@Transactional
	public MenuRecommendInfoDto getMenuRecommendInfo(Integer menuId, Integer memberId) {
		if(menuId==null) return null;
		
		Optional<Menu> om = meRepo.findById(menuId);
		Optional<Member> omb = mRepo.findById(memberId);
		if(om.isEmpty()||omb.isEmpty()) return null;
		
		// 메뉴 선택 수 증가
		Menu m = om.get();
		int cnt = m.getCount();
		m.setCount(cnt++);
		meRepo.save(m);
		
		// 히스토리 남기기
		HistoryId hId = new HistoryId(memberId, LocalDateTime.now());
		History h = new History();
		h.setHistoryId(hId);
		h.setMember(omb.get());
		h.setMenu(m);
		hRepo.save(h);
		
		return new MenuRecommendInfoDto(m.getPlace(),m.getName());
	}
	
	/**
	 * 메뉴가 조회된 횟수 조회.
	 * @return
	 */
	public int getMenuCnt() {
		return meRepo.countTotalMenu();
	}
}
