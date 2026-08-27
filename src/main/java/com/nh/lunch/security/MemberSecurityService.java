package com.nh.lunch.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nh.lunch.member.Member;
import com.nh.lunch.member.MemberRepository;

@Service
public class MemberSecurityService implements UserDetailsService{
	@Autowired
	MemberRepository memberRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> om = memberRepo.findById(Integer.valueOf(username));
		if(om.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없음.");
		}
		Member m = om.get();
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		//authorities : 사용자가 가진 권한 목록 (예. ROLE_USER, ROLE_ADIMIN)
		// 이 '권한'들은 인가 과정에서 사용
//		if("admin".equals(username)) {
//			authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
//		} else {
//			authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
//		}
		
		return new User(m.getMemberId().toString(), m.getPw(), authorities);
	}
}
