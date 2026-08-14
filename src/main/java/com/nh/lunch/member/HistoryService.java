package com.nh.lunch.member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nh.lunch.menu.Menu;
import com.nh.lunch.menu.MenuRepository;

import jakarta.transaction.Transactional;

@Service
public class HistoryService {
	@Autowired
	private HistoryRepository hRepo;
	@Autowired
	private MenuRepository meRepo;
	@Autowired
	private MemberRepository mRepo;
	
	/**
	 * 히스토리 삽입.
	 * @param memberId : 삽입하는 멤버의 id
	 * @param menuId : 삽입한 메뉴의 id
	 * @return : 삽입 성공 true, 실패 false
	 */
	@Transactional
	public boolean insertHistory(int memberId, int menuId) {
		
		Optional<Menu> ome = meRepo.findById(menuId);
		if(ome.isEmpty()) {
			// 메뉴가 있지 않으면.
			return false;
		}
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) {
			// 멤버가 있지 않으면.
			return false;
		}
		// 메뉴나 멤버가 존재하면
		History h = new History();
		h.setHistoryId(new HistoryId(memberId, LocalDateTime.now()));
		h.setMember(om.get());
		h.setMenu(ome.get());
		hRepo.save(h);
		return true;
	}
	
	/**
	 * 해당 멤버가 선택한 메뉴명들 조회
	 * @param memberId
	 * @return : 조회한 메뉴명 List, 해당 멤버가 없다면 null
	 */
	@Transactional
	public List<String> getHistorysByMemberId(int memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) {
			// 멤버가 있지 않으면.
			return null;
		}
		Member m = om.get();
		
		List<History> historys = m.getHistory();
		List<String> menus = new ArrayList<>();
		for(History h : historys) {
			menus.add(h.getMenu().getName());
		}
		
		return menus;
	}
	
}









