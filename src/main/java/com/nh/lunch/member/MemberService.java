package com.nh.lunch.member;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
	@Autowired
	private MemberRepository mRepo;
	@Autowired
	private PasswordEncoder pwEncoder;
	
	/**
	 * memberId로 멤버 정보 가져오기.
	 * @param memberId : 가져올 멤버의 PK 
	 * @return : memberDto(memberId, email, pw, passwordKey, exDate, chatKey), 못가져오면 null
	 */
	public MemberDto getMemberById(Integer memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) {
			// 해당 member가 존재하지 않는 경우.
			return null;
		}
		return new MemberDto(om.get());
	}
	
	/**
	 * 이메일이 존재하는 멤버인지 
	 * @param email
	 * @return true이면 동일한 메일이 존재, false이면 메일 비존재 
	 */
	public boolean isExistMemberByEmail(String email) {
		Member m = mRepo.findByEmail(email);
		if(m == null) { // 동일한 멤버가 존재하지 않으면
			return false;
		} else { // 동일한 멤버가 존재한다면.
			return true;
		}
	}
	
	/**
	 * 메일과 비밀번호가 일치하는지.
	 * @param email
	 * @param pw
	 * @return true이면 멤버 존재, false이면 멤버 비존재
	 */
	public boolean isExistMemberByEmailAndPw(String email, String pw) {
		Member m = mRepo.findByEmailAndPw(email, pw);
		
		if(m == null) { // 메일과 비밀번호가 일치하지 않는다면,
			return false;
		} else { // 메일과 비밀번호가 일치한다면,
			return true;
		}
	}
	
	/**
	 * 멤버 삽입.
	 * @param email
	 * @param pw
	 * @return 멤버 삽입이되면 true, 이미 존재하는 멤버면 false
	 */
	public boolean insertMember(String email, String pw) {
		
		if(isExistMemberByEmail(email)) { 
			// 이미 존재하는 멤버면 false 리턴
			return false;
		} 
		// 이미 존재안하면 멤버 삽입, true리턴
		Member m = new Member();
		m.setEmail(email);
		m.setPw(pwEncoder.encode(pw));
		mRepo.save(m);
		
		return true;
	}
	
	/**
	 * 비밀번호 업데이트
	 * @param memberId : 업데이트 당할 memberId
	 * @param pw : 바꿀 비밀번호
	 * @return : 해당 member가 존재하지 않을 경우 false, 존재하면 true;
	 */
	public boolean updatePw(int memberId, String pw) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return false;
		}
		// memberId가 있을 경우. 해당 pw 업데이트.
		Member m = om.get();
		m.setPw(pw);
		mRepo.save(m);
		return true;
	} 
	
	/**
	 * 비밀번호 재설정 키 업데이트
	 * @param memberId : 키 넣을 멤버의 id (memberId)
	 * @return : 재설정 키 , 해당 멤버가 없으면 null
	 */
	public String updatePwKey(int memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return null;
		}
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		// 만료시간 (현재시간 + 10분)
		LocalDateTime exDate = LocalDateTime.now().plusMinutes(10);
		
		// memberId가 있을 경우. 해당 pw 업데이트.
		Member m = om.get();
		m.setPasswordKey(sb.toString());
		m.setExDate(exDate);
		mRepo.save(m);
		return sb.toString();
	}
	
	/**
	 * 비밀번호 재설정 키 맞는지 확인.
	 * @param memberId : 확인할 사람의 id (member_id)
	 * @param key : input 비교할 key 
	 * @return : 만료일과 만료되지 않고 키가 동일하면 true, 아니면 false
	 */
	public boolean isEqualsPwKey(int memberId, String key) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return false;
		}
		
		MemberDto memberDto = new MemberDto(om.get());
		LocalDateTime dtoExDate = memberDto.getExDate();
		String dtoPwKey = memberDto.getPasswordKey();
		LocalDateTime now = LocalDateTime.now();
		
		if(key.equals(dtoPwKey) && dtoExDate.isAfter(now)) {
			// 현재 DB의 값과 input의 값이 같고, 현재 시간보다 만료가 안되면 
			return true;
		} 
		return false;
	}
	
	/**
	 * 채팅 키 값 생성.
	 * @param memberId : 생성한 사람의 memberId
	 * @return 재설정 키 , 해당 멤버가 없으면 null
	 */
	public String updateChatKey(int memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return null;
		}
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		// 만료시간 (현재시간 + 10분)
		LocalDateTime exDate = LocalDateTime.now().plusMinutes(10);
		
		// memberId가 있을 경우. 해당 pw 업데이트.
		Member m = om.get();
		m.setChatKey(sb.toString());
		m.setExDate(exDate);
		mRepo.save(m);
		return sb.toString();
	}
	
	/**
	 * 채팅 키 값 맞는지 확인.
	 * @param memberId : 확인할 사람의 id (member_id)
	 * @param key : input 비교할 key 
	 * @return : 만료일과 만료되지 않고 키가 동일하면 true, 아니면 false
	 */
	public boolean isEqualsChatKey(int memberId, String key) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return false;
		}
		
		MemberDto memberDto = new MemberDto(om.get());
		LocalDateTime dtoExDate = memberDto.getExDate();
		String dtoChatKey = memberDto.getChatKey();
		LocalDateTime now = LocalDateTime.now();
		
		if(key.equals(dtoChatKey) && dtoExDate.isAfter(now)) {
			// 현재 DB의 값과 input의 값이 같고, 현재 시간보다 만료가 안되면 
			return true;
		} 
		return false;
	}
	
}









