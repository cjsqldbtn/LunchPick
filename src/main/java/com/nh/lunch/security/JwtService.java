package com.nh.lunch.security;

import java.security.Key;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class JwtService { 	// JWT 토큰 발급 및 검증을 담당.
	static final long EXPIRATION_TIME = 86_400_000; // msec단위. 1일
	static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 비밀 키 생성
	
	// 서명된 JWT 토큰 생성 (여기서는 JWT 토큰의 ID만 담음.)
	public String getToken(String username, Integer memberId) {
		String token = Jwts.builder()
							.setSubject(username)
							.claim("memberId", memberId)
							.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
							.signWith(key)
							.compact();
		return token;
	}
	
	//(요청 권한 부여) 요청 헤더에서 토큰을 가져와서, 토큰으르 확인하고 username(아이디)을 얻음.
	public String getAuthUser(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		System.out.println("(getAuthUser) token : " + token);
		
		if(token != null && token.startsWith("Bearer")) {
			token = token.substring(7); // "Bearer "를 제거.
			System.out.println("(JwtSvc) token:[" + token + "]");
			
			String username = Jwts.parserBuilder()
								.setSigningKey(key)
								.build()
								.parseClaimsJws(token)
								.getBody()
								.getSubject();
			System.out.println("(getAuthUser) username : " + username);
			if(username != null) return username;
		}
		return null;
	}
	
	//(요청 권한 부여) 요청 헤더에서 토큰을 가져와서, 토큰으르 확인하고 memberId을 얻음.
	public Integer getMemberId(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
            return claims.get("memberId", Integer.class);
        }
        return null;
    }

}










