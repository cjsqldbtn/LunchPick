package com.nh.lunch.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nh.lunch.security.JwtAccountCredentials;
import com.nh.lunch.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@PropertySource("classpath:secret.properties")
@RequestMapping("/member")
@RestController
public class MemberController {
	@Autowired
	private JwtService jwtSvc;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private MemberService mSvc;
	
	@Value("${kakao.client.id}")
    private String KakaoClientId;
	@Value("${kakao.client.secret}")
	private String KakaoClientSecret;
	@Value("${naver.client.id}")
    private String NaverClientId;
	@Value("${naver.client.secret}")
	private String NaverClientSecret;
	
	// 로그인 버튼 클릭.
	@PostMapping("/login")
	public ResponseEntity<?> getToken(@RequestBody JwtAccountCredentials credentials) {
        //System.out.println("암호화값: " + new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("c"));
        try {
	        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());
	        Authentication auth = authManager.authenticate(creds);
	        String email = auth.getName();
	        MemberDto member = mSvc.getMemberByEmail(email);
	        
	        
	        String jwts = jwtSvc.getToken(auth.getName(), member.getMemberId());
	        System.out.println("(MemberController) jwts: " + jwts);

	        return ResponseEntity.ok()
	                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
	                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
	                .build();

	    } catch (Exception e) {
	        System.out.println("=== 로그인 중 예외 발생! ===");
	        e.printStackTrace(); // 콘솔에 에러 스택트레이스 출력
	        return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
	    }
	}
	
	@GetMapping("/userInfo")
	public ResponseEntity<Map<String,String>> getUserInfo(@RequestHeader("Authorization") String authorization) {
		System.out.println("(userinfo)" + authorization);
		
		Map<String, String> mapRet = new HashMap<>();
		
		return ResponseEntity.ok(mapRet);
	}

	@GetMapping("/kakaoLogin")
	public void redirectToKakao(HttpServletResponse response) {
        
        String redirectUrl = "http://localhost:3000/LunchPick/kakaologin";
        String naverUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + KakaoClientId + "&redirect_uri=" + redirectUrl;
        	
        try {
			response.sendRedirect(naverUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
	
	// 리액트에서 code를 보내오면 JWT 토큰 리턴
    @PostMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {
		
        try {
            // 소셜 로그인 처리 후, JWT 토큰 발급
            String jwtToken = mSvc.processSocialLogin(code, null, "kakao");

            //리액트로 JWT 토큰 반환 (기존 로그인 방식과 동일)
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                    .body(Map.of("message", "카카오 로그인 성공"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("소셜 로그인 실패: " + e.getMessage());
        }
    }
    
    @GetMapping("/naverLogin")
    public void redirectToNaver(HttpServletResponse response) {
        
        String redirectUrl = "http://localhost:3000/LunchPick/naverlogin";
        String state = UUID.randomUUID().toString();
        String naverUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=" + NaverClientId + "&redirect_uri=" + redirectUrl + "&state=" + state;
        	
        try {
			response.sendRedirect(naverUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 

    @PostMapping("/naverLogin")
    public ResponseEntity<?> naverLogin(@RequestParam("code") String code, @RequestParam(value = "state", required = false) String state) {
    	
    	try {
            // 소셜 로그인 처리 후, JWT 토큰 발급
            String jwtToken = mSvc.processSocialLogin(code, state, "naver");

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                    .body(Map.of("message", "네이버 로그인 성공"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("소셜 로그인 실패: " + e.getMessage());
        }
    }
    
    //채팅 키 만들기.
    @PostMapping("/createChatKey")
    public String createChatKey(HttpServletRequest request) {
        Integer username = jwtSvc.getMemberId(request);
        
        if (username == null) {
            return "FAIL: 유효하지 않은 토큰입니다.";
        }

        String chatKey = mSvc.updateChatKey(username);
        
        return chatKey; // 생성된 채팅키 문자열 반환 (예: "ROOM_12345")
    }

}
