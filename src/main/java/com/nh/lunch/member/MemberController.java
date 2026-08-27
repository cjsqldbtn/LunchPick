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
	        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
	        Authentication auth = authManager.authenticate(creds);
	        String jwts = jwtSvc.getToken(auth.getName());
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

	@GetMapping("kakaoLogin")
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


//	// 신규 회원 인증번호 맞는지 체크
//	@GetMapping("/joinCode")
//	public String checkAuthCode(RedirectAttributes rttr, HttpSession session, String inputKey) {
//		// 지금 현재 세션에 저장된 key여야지만 접근 가능
//		String keySession = (String) session.getAttribute("key");
//		// System.out.println("발급된 키 "+keySession);
//		// System.out.println("입력된 키 "+inputKey);
//		if (!keySession.equals(inputKey)) {
//			rttr.addFlashAttribute("msg", "인증번호가 일치하지 않습니다. 다시 시도해주세요.");
//		} else {
//			int memberId = mSvc.addMember((String) session.getAttribute("email"));
//			// System.out.println(memberId);
//			session.setAttribute("loginId", memberId);
//			rttr.addFlashAttribute("msg", "회원 가입이 완료 되었습니다. 꼭 비밀번호를 변경해주세요.");
//			return "SetPw";
//		}
//
//		return "redirect:/";
//	}
}
