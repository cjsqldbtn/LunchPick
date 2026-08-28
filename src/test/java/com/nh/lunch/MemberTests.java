package com.nh.lunch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.nh.lunch.member.Member;
import com.nh.lunch.member.MemberDto;
import com.nh.lunch.member.MemberRepository;
import com.nh.lunch.member.MemberService;

@SpringBootTest
@ActiveProfiles("test")
class MemberTests {
	@Autowired
	private MemberRepository mRepo;
	@Autowired
	private MemberService mSvc;
	
	/************Repo************/
	// 멤버 삽입.
	@Test
	@Transactional
	void testMemberInsertDummy() {
		// 1) Given
		String email = "a";
		String pw = "a";
		
		// 2) When
		Member m = new Member();
		m.setEmail(email);
		m.setPw(pw);
		mRepo.save(m);
		
		//3) Then : 오류 없으면 성공!
	}
	
	// email로 멤버 정보 조회
	@Test
	void testFindByEmail() {
		// 1) Given
		String email = "a";
		
		// 2) When
		Member member = mRepo.findByEmail(email);
		
		// 3) Then
		assertNotNull(member, "member가 null이면 안됨.");
//		System.out.println("조회된 member_id" + member.getMemberId());
//		System.out.println("조회된 pw" + member.getPw());
	}
	
	// email & pw로 멤버 정보 조회
	@Test
	void testFindByEmailAndPw() {
		// 1) Given
		String email = "a";
		String pw = "a";
		
		// 2) When
		Member member = mRepo.findByEmailAndPw(email, pw);
		
		// 3) Then
		assertNotNull(member, "member가 null이면 안됨.");
//		System.out.println("조회된 member_id" + member.getMemberId());
//		System.out.println("조회된 pw" + member.getPw());
	}
	
	// passwordKey로 멤버 정보 조회
	@Test
	void testFindByPasswordKey() {
		// 1) Given
		String email = "a";
		String pw = "a";
		
		// 2) When
		Member member = mRepo.findByEmailAndPw(email, pw);
		
		// 3) Then
		assertNotNull(member, "member가 null이면 안됨.");
//		System.out.println("조회된 member_id" + member.getMemberId());
//		System.out.println("조회된 pw" + member.getPw());
	}
	
	/****************Service****************/
	// memberId로 멤버 정보 조회.
	@Test
	void testGetMemberById() {
		// 1) Given 
		int memberId = 1;
		
		// 2) When
		MemberDto memberDto = mSvc.getMemberById(memberId);
		
		// 3) Then
		assertNotNull(memberDto, "memberDto가 null이면 안됨.");
//		System.out.println("조회된 memberDto: " + memberDto.getEmail());
	}
	
	// 이메일이 존재하는지.
	@Test
	void testIsExistMemberByEmail() {
		// 1) Given
		String email = "c";
		
		// 2) When
		boolean result = mSvc.isExistMemberByEmail(email);
		
		// 3) Then : 메일이 존재하면 true, 존재하지 않으면 false;
//		System.out.println(result);
	}
	
	// 메일과 비밀번호가 일치하는지.
	@Test
	void testIsExistMemberByByEmailAndPw() {
		// 1) Given
		String email = "a";
		String pw = "c";
		
		// 2) When
		boolean result = mSvc.isExistMemberByEmailAndPw(email, pw);
		
		// 3) Then : 일치하면 true, 일치하지 않으면 false;
//		System.out.println(result);
	}
	
	// 멤버 삽입.
	@Test 
	@Transactional
	void testInsertMember() {
		// 1) Given
		String email = "c";
		String pw = "c";
		
		// 2) When
		boolean result = mSvc.insertMember(email, pw);
		
		// 3) Then : 삽입이 성공되면 true, 성공 안되면 false;
		assertTrue(result, "멤버 삽입이 안됨. (해당 email는 이미 존재)");
//		System.out.println(result);
	}
	
	// 비밀번호 업데이트 
	@Test
	@Transactional
	void testUpdatePw() {
		// 1) Given
		int memberId = 1;
		String pw = "b";
		
		// 2) When 
		boolean result = mSvc.updatePw(memberId, pw);
		
		// 3) Then : 업데이트 실패하면 false, 성공하면 true
		assertTrue(result, "비밀번호 업데이트 실패 (해당 Id는 존재하지 않음)");
//		System.out.println(result);
	}
	
	// 비밀번호 재설정 키 업데이트
	@Test
	@Transactional
	void testUpdatePwKey() {
		// 1) Given
		int memberId = 1;
		
		// 2) When
		String key = mSvc.updatePwKey(memberId);
		
		// 3) Then : 해당 Id가 존재하지 않으면 null 
		assertNotNull(key, "key는 Null이면 안됨.");
//		System.out.println(key);
	}
	
	// 비밀번호 재설정 키 맞는지 조회.
	@Test
	void testIsEqualsPwKey() {
		// 1) Given
		int memberId = 1; 
		String key = "ydVuZz";
		
		// 2) When
		boolean result = mSvc.isEqualsPwKey(memberId, key);
		
		// 3) Then : 만료시간이 만료되지 않고 키가 동일하면 true, 아니면 false
		assertTrue(result, "키가 동일하지 않거나 만료시간이 만료됨.");
//		System.out.print(result);
	}
	
	// 채팅 재설정 키 업데이트
	@Test
	@Transactional
	void testUpdateChatKey() {
		// 1) Given
		int memberId = 1;
		
		// 2) When
		String key = mSvc.updateChatKey(memberId);
		
		// 3) Then : 해당 Id가 존재하지 않으면 null 
		assertNotNull(key, "key는 Null이면 안됨.");
//		System.out.println(key);
	}
	
	// 채팅 재설정 키 맞는지 조회.
	@Test
	void testIsEqualsChatKey() {
		// 1) Given
		int memberId = 1; 
		String key = "xohAs";
		
		// 2) When
		boolean result = mSvc.isEqualsChatKey(memberId, key);
		
		// 3) Then : 만료시간이 만료되지 않고 키가 동일하면 true, 아니면 false
		assertTrue(result, "키가 동일하지 않거나 만료시간이 만료됨.");
//		System.out.println(result);
	}
		
}
