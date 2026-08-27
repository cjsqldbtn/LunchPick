package com.nh.lunch.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nh.lunch.security.JwtAccountCredentials;
import com.nh.lunch.security.JwtService;

@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private JwtService jwtSvc;
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/login")
	public ResponseEntity<?> getToken(@RequestBody JwtAccountCredentials credentials) {
		
		UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(credentials.getUsername(),credentials.getPassword());
		Authentication auth = authManager.authenticate(creds);
		String jwts = jwtSvc.getToken(auth.getName());
		System.out.println("(MemberController) jwts:" + jwts);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
				.build();
	}
	
	@GetMapping("/userInfo")
	public ResponseEntity<Map<String,String>> getUserInfo(@RequestHeader("Authorization") String authorization) {
		System.out.println("(userinfo)" + authorization);
		
		Map<String, String> mapRet = new HashMap<>();
		
		
		return ResponseEntity.ok(mapRet);
	}
}
