package com.nh.lunch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.nh.lunch.member.Member;
import com.nh.lunch.member.MemberRepository;

@SpringBootTest
class MemberTests {
	@Autowired
	private MemberRepository mRepo;
	
	@Test
	void testMemberInsertDummy() {
		Member m = new Member();
		m.setEmail("b");
		m.setPw("b");
		mRepo.save(m);
	}
	
	
}
