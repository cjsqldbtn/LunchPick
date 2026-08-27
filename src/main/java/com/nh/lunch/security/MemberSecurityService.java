package com.nh.lunch.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
		Member m = memberRepo.findByEmail(username);
		if(m==null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없음.");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 기본 권한 부여

        // Username으로 email이나 ID 등 식별자를 넘겨줍니다.
        return new User(m.getEmail(), m.getPw(), authorities);
	}
}
