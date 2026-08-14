package com.nh.lunch.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	Member findByEmail(String email);
	Member findByEmailAndPw(String email, String pw);
	Member findByPasswordKey(String passwordKey);
}
