package com.nh.lunch.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private final JwtService jwtSvc;
	private final MemberSecurityService userDetailsService;
	
	JwtAuthenticationFilter(JwtService jwtSvc, MemberSecurityService userDetailsService) {
		this.jwtSvc = jwtSvc;
		this.userDetailsService = userDetailsService;
	}

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//(요청 헤더로부터) Authorization에 있는 토큰을 가져옴. 
		String jwts = request.getHeader(HttpHeaders.AUTHORIZATION); 
		
		
		if(jwts != null) {
			// 토큰을 확인하고 사용자를 얻음.
			String username = jwtSvc.getAuthUser(request);
			
			// 인증.
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
			
			// SecurityContextHolder에 담아둠.
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		// 나머지 필터 실행을 계속함.
		filterChain.doFilter(request, response);
	}
	
}
