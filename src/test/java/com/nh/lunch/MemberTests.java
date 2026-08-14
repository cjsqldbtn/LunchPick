package com.nh.lunch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.nh.lunch.member.Member;
import com.nh.lunch.member.MemberRepository;

@SpringBootTest
class MemberTests {
	@Autowired
	private MemberRepository mRepo;
	
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
		System.out.println("조회된 member_id" + member.getMemberId());
		System.out.println("조회된 pw" + member.getPw());
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
		System.out.println("조회된 member_id" + member.getMemberId());
		System.out.println("조회된 pw" + member.getPw());
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
		System.out.println("조회된 member_id" + member.getMemberId());
		System.out.println("조회된 pw" + member.getPw());
	}
		
}
