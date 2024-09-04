package com.mysite.login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mysite.login.entity.GoogleInfResponse;
import com.mysite.login.entity.GoogleRequest;
import com.mysite.login.entity.GoogleResponse;
import com.mysite.login.entity.Member;
import com.mysite.login.service.MemberService;

@RestController
@RequestMapping("/api/v1/oauth2")
public class GoogleController {

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.pw}")
    private String googleClientPw;

    @Value("${google.client.redirect-uri}")
    private String googleRedirectUri;

    @Autowired
    private MemberService memberService; // MemberService 주입

    @GetMapping("/google")
    public ResponseEntity<Map<String, String>> getGoogleLoginUrl() {
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/google/callback", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> loginGoogle(@RequestParam(value = "code") String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        // Google OAuth2 토큰 요청
        GoogleRequest googleOAuthRequestParam = GoogleRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri(googleRedirectUri)
                .grantType("authorization_code")
                .build();
        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", googleOAuthRequestParam, GoogleResponse.class);
        String jwtToken = resultEntity.getBody().getId_token();
        
        // Google 사용자 정보 요청
        Map<String, String> map = new HashMap<>();
        map.put("id_token", jwtToken);
        ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/tokeninfo", map, GoogleInfResponse.class);
        String email = resultEntity2.getBody().getEmail();
        String username = resultEntity2.getBody().getName();

        // 사용자 정보 데이터베이스에 저장
        Member member = memberService.findByEmail(email);
        if (member == null) {
            member = new Member();
            member.setUsername(username);
            member.setEmail(email);
            member.setPassword(""); // Google 로그인에서는 비밀번호가 필요 없음
            member.setRole("USER");
            memberService.saveMember(member);
        }

        // 세션 생성 (여기서는 JWT를 반환하거나, 세션을 생성하는 로직을 추가)
        // Map<String, String> response = new HashMap<>();
        // response.put("id_token", jwtToken);

        Map<String, String> response = new HashMap<>();
        response.put("id_token", jwtToken);
        return ResponseEntity.ok(response);
    }
}
