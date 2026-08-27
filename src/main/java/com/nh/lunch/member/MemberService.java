package com.nh.lunch.member;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@PropertySource("classpath:secret.properties")
@Service
public class MemberService {
	@Autowired
	private MemberRepository mRepo;
	@Autowired
	private PasswordEncoder pwEncoder;
	
	@Value("${kakao.client.id}")
    private String KakaoClientId;
	@Value("${kakao.client.secret}")
	private String KakaoClientSecret;
	@Value("${naver.client.id}")
    private String NaverClientId;
	@Value("${naver.client.secret}")
	private String NaverClientSecret;
	
	/**
	 * memberId로 멤버 정보 가져오기.
	 * @param memberId : 가져올 멤버의 PK 
	 * @return : memberDto(memberId, email, pw, passwordKey, exDate, chatKey), 못가져오면 null
	 */
	public MemberDto getMemberById(Integer memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) {
			// 해당 member가 존재하지 않는 경우.
			return null;
		}
		return new MemberDto(om.get());
	}
	
	/**
	 * email로 멤버 정보 가져오기.
	 * @param email : 가져올 멤버의 email
	 * @return : memberDto(memberId, email, pw, passwordKey, exDate, chatKey), 못가져오면 null
	 */
	public MemberDto getMemberByEmail(String email) {
		Member m = mRepo.findByEmail(email);
		if(m==null) {
			// 해당 member가 존재하지 않는 경우.
			return null;
		}
		return new MemberDto(m);
	}
	
	/**
	 * 이메일이 존재하는 멤버인지 
	 * @param email
	 * @return true이면 동일한 메일이 존재, false이면 메일 비존재 
	 */
	public boolean isExistMemberByEmail(String email) {
		Member m = mRepo.findByEmail(email);
		if(m == null) { // 동일한 멤버가 존재하지 않으면
			return false;
		} else { // 동일한 멤버가 존재한다면.
			return true;
		}
	}
	
	/**
	 * 메일과 비밀번호가 일치하는지.
	 * @param email
	 * @param pw
	 * @return true이면 멤버 존재, false이면 멤버 비존재
	 */
	public boolean isExistMemberByEmailAndPw(String email, String pw) {
		Member m = mRepo.findByEmailAndPw(email, pw);
		
		if(m == null) { // 메일과 비밀번호가 일치하지 않는다면,
			return false;
		} else { // 메일과 비밀번호가 일치한다면,
			return true;
		}
	}
	
	/**
	 * 멤버 삽입.
	 * @param email
	 * @param pw
	 * @return 멤버 삽입이되면 true, 이미 존재하는 멤버면 false
	 */
	public boolean insertMember(String email, String pw) {
		
		if(isExistMemberByEmail(email)) { 
			// 이미 존재하는 멤버면 false 리턴
			return false;
		} 
		// 이미 존재안하면 멤버 삽입, true리턴
		Member m = new Member();
		m.setEmail(email);
		m.setPw(pwEncoder.encode(pw));
		mRepo.save(m);
		
		return true;
	}
	
	/**
	 * 비밀번호 업데이트
	 * @param memberId : 업데이트 당할 memberId
	 * @param pw : 바꿀 비밀번호
	 * @return : 해당 member가 존재하지 않을 경우 false, 존재하면 true;
	 */
	public boolean updatePw(int memberId, String pw) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return false;
		}
		// memberId가 있을 경우. 해당 pw 업데이트.
		Member m = om.get();
		m.setPw(pw);
		mRepo.save(m);
		return true;
	} 
	
	/**
	 * 비밀번호 재설정 키 업데이트
	 * @param memberId : 키 넣을 멤버의 id (memberId)
	 * @return : 재설정 키 , 해당 멤버가 없으면 null
	 */
	public String updatePwKey(int memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return null;
		}
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		// 만료시간 (현재시간 + 10분)
		LocalDateTime exDate = LocalDateTime.now().plusMinutes(10);
		
		// memberId가 있을 경우. 해당 pw 업데이트.
		Member m = om.get();
		m.setPasswordKey(sb.toString());
		m.setExDate(exDate);
		mRepo.save(m);
		return sb.toString();
	}
	
	/**
	 * 비밀번호 재설정 키 맞는지 확인.
	 * @param memberId : 확인할 사람의 id (member_id)
	 * @param key : input 비교할 key 
	 * @return : 만료일과 만료되지 않고 키가 동일하면 true, 아니면 false
	 */
	public boolean isEqualsPwKey(int memberId, String key) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return false;
		}
		
		MemberDto memberDto = new MemberDto(om.get());
		LocalDateTime dtoExDate = memberDto.getExDate();
		String dtoPwKey = memberDto.getPasswordKey();
		LocalDateTime now = LocalDateTime.now();
		
		if(key.equals(dtoPwKey) && dtoExDate.isAfter(now)) {
			// 현재 DB의 값과 input의 값이 같고, 현재 시간보다 만료가 안되면 
			return true;
		} 
		return false;
	}
	
	/**
	 * 채팅 키 값 생성.
	 * @param memberId : 생성한 사람의 memberId
	 * @return 재설정 키 , 해당 멤버가 없으면 null
	 */
	public String updateChatKey(int memberId) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return null;
		}
		
		// 랜덤키
		StringBuffer sb = new StringBuffer();
		while(sb.length()<6) {
			int temp = (int)(Math.random()*75) + 48;
			if(temp<58||(temp>64&&temp<91)||(temp>96)) sb.append((char)temp);
		}
		// 만료시간 (현재시간 + 10분)
		LocalDateTime exDate = LocalDateTime.now().plusMinutes(10);
		
		// memberId가 있을 경우. 해당 pw 업데이트.
		Member m = om.get();
		m.setChatKey(sb.toString());
		m.setExDate(exDate);
		mRepo.save(m);
		return sb.toString();
	}
	
	/**
	 * 채팅 키 값 맞는지 확인.
	 * @param memberId : 확인할 사람의 id (member_id)
	 * @param key : input 비교할 key 
	 * @return : 만료일과 만료되지 않고 키가 동일하면 true, 아니면 false
	 */
	public boolean isEqualsChatKey(int memberId, String key) {
		Optional<Member> om = mRepo.findById(memberId);
		if(om.isEmpty()) { 
			// memberId가 없을 경우.
			return false;
		}
		
		MemberDto memberDto = new MemberDto(om.get());
		LocalDateTime dtoExDate = memberDto.getExDate();
		String dtoChatKey = memberDto.getChatKey();
		LocalDateTime now = LocalDateTime.now();
		
		if(key.equals(dtoChatKey) && dtoExDate.isAfter(now)) {
			// 현재 DB의 값과 input의 값이 같고, 현재 시간보다 만료가 안되면 
			return true;
		} 
		return false;
	}
	
	/**
	 * 카카오로 email 조회
	 * @param code
	 * @return email
	 */
	public String getEmailByKakao(String code, String mapping) {
		// 코드로 토큰 발급
	    String authCode = code;
	    
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    // body
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "authorization_code");
	    body.add("client_id", KakaoClientId);
	    body.add("redirect_uri", "http://52.199.216.149:9090/TravelPlanner/kakaologin/"+mapping);
	    body.add("code", authCode);
	    body.add("client_secret", KakaoClientSecret);
	    
	    // Http요청 객체
	    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
	    // Kakao API 호출
	    ResponseEntity<JsonNode> response =
	        new RestTemplate().exchange(
	            "https://kauth.kakao.com/oauth/token",
	            HttpMethod.POST,
	            httpEntity,
	            JsonNode.class);
	    
	    JsonNode jsonNode = response.getBody();
	    
	    // 토큰으로 이메일 조회
	    String token = jsonNode.get("access_token").asText();
	    
	    // 헤더
	    headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    headers.add("Authorization", "bearer " + token);
	    
	    // Http요청 객체
	    httpEntity = new HttpEntity<>(headers);
	    
	    // Kakao API 호출
	    response = new RestTemplate().exchange(
	            "https://kapi.kakao.com/v2/user/me",
	            HttpMethod.GET,
	            new HttpEntity<>(headers),
	            JsonNode.class);
	    
	    //System.out.println(response.getBody());
	    jsonNode = response.getBody();
	    String email = jsonNode.get("kakao_account").get("email").asText();
		return email;
	}
	
	/**
	 * 네이버로 email 조회
	 * @param code
	 * @return email
	 */
	public String getEmailByNaver(String code, String state) {
		// 코드로 토큰 발급
	    
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    // body
	    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	    body.add("grant_type", "authorization_code");
	    body.add("client_id", NaverClientId);
	    body.add("client_secret", NaverClientSecret);
	    body.add("code", code);
	    body.add("state", state);
	    
	    
	    body.add("redirect_uri", "http://52.199.216.149:9090/TravelPlanner/naverlogin/mainHome");
	    
	    
	    // Http요청 객체
	    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
	    // Naver API 호출
	    ResponseEntity<JsonNode> response =
	        new RestTemplate().exchange(
	            "https://nid.naver.com/oauth2.0/token",
	            HttpMethod.POST,
	            httpEntity,
	            JsonNode.class);
	    
	    JsonNode jsonNode = response.getBody();
	    
	    // 토큰으로 이메일 조회
	    String token = jsonNode.get("access_token").asText();
	    
	    // 헤더
	    headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    headers.add("Authorization", "Bearer " + token);
	    
	    // Http요청 객체
	    httpEntity = new HttpEntity<>(headers);
	    
	    // Naver API 호출
	    response = new RestTemplate().exchange(
	            "https://openapi.naver.com/v1/nid/me",
	            HttpMethod.GET,
	            new HttpEntity<>(headers),
	            JsonNode.class);
	    
	    //System.out.println(response.getBody());
	    jsonNode = response.getBody();
	    String email = jsonNode.get("response").get("email").asText();
		return email;
	}
	
}









