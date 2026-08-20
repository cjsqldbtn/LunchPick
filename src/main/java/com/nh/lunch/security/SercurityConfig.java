package com.nh.lunch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SercurityConfig {
	@Bean 									
	SecurityFilterChain	securityFilterChain(HttpSecurity http) throws Exception{ //Chain 다음꺼 실행하고, 다음꺼 실행하느 ㄴ즉,순서대로 하는 친구.()
		http
		.authorizeHttpRequests(
				(authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers("/**") 
					.permitAll() // 로그인이 없어도 모든 사람이 접근 가능.
		).formLogin(
				(formLoginConfig) -> formLoginConfig
				.loginPage("/member/login") // -> Spring security에게 로그인 페이지의 url을 알려줌 , 만약 인증이 필요한 요청에 인증X 접근시, /member/login으로 리다이렉트.
				.usernameParameter("id") // <form>에서 ID파라미터의 name속성 설정.
				.passwordParameter("pw") //<form>에서 PW파라미터의 name속성 설정.
				.defaultSuccessUrl("/") // 로그인 성공 시, redirect Url 설정.
		).logout(
				(logoutConfig) -> logoutConfig
				.logoutUrl("/member/logout")
				.invalidateHttpSession(true)
				.logoutSuccessUrl("/")
		);
		return http.build(); //build 패턴을 리턴 
	}
	
	@Bean
	PasswordEncoder pwEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration authConfig) {
		return authConfig.getAuthenticationManager();
	}
}
