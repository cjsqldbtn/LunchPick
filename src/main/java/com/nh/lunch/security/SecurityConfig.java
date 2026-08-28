package com.nh.lunch.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//같은 페키지 않이라 컴포텉느 스킨 필요없음ㅁ.
@Configuration // 스프링의 환경설정 클래스임을 의미.
@EnableWebSecurity // 모든 요청 URL이 스프링시큐리티의 제어를 받도록 함. (=스프링 시큐리티 활성화)
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig {
	@Autowired
	private JwtService jwtSvc;
	@Autowired
	private MemberSecurityService userDetailsSerivce;
	
	 // return 되는데 빈으로 등록 디스패터 전에 들어가기 전에 거르는 친구 filer, 들어와서 컨트롤럴를 들어오기 전에 거르는거 interceper
	@Bean 									// HttpSecurity : Spring security 설정 역할
	SecurityFilterChain	securityFilterChain(HttpSecurity http) throws Exception{ //Chain 다음꺼 실행하고, 다음꺼 실행하느 ㄴ즉,순서대로 하는 친구.()
		http
		.csrf(csrf -> csrf.disable()) // CSRF 방어기능 비활성화.
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(
				(authorizeHttpRequests) -> authorizeHttpRequests
					.anyRequest() // 어느접근이라도 
					.permitAll()  // ㄱㅊ
		)
		.addFilterBefore(
				new JwtAuthenticationFilter(jwtSvc,userDetailsSerivce),
				UsernamePasswordAuthenticationFilter.class
		)
		.logout(
				logout -> logout.disable()
		);
		return http.build(); //build 패턴을 리턴 
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration authConfig) {
		return authConfig.getAuthenticationManager();
	}
}
