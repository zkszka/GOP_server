package com.mysite.login.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private MemberService memberService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/google")
    public ResponseEntity<Map<String, String>> getGoogleLoginUrl() {
        String url = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Map<String, String>> loginGoogle(@RequestParam(value = "code") String authCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Received authCode: " + authCode); // Debugging

        RestTemplate restTemplate = new RestTemplate();

        // Google OAuth2 토큰 요청
        GoogleRequest googleOAuthRequestParam = GoogleRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri(googleRedirectUri)
                .grantType("authorization_code")
                .build();
        ResponseEntity<GoogleResponse> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", googleOAuthRequestParam, GoogleResponse.class);
        String jwtToken = tokenResponse.getBody().getId_token();

        // Google 사용자 정보 요청
        Map<String, String> map = new HashMap<>();
        map.put("id_token", jwtToken);
        ResponseEntity<GoogleInfResponse> userInfoResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/tokeninfo", map, GoogleInfResponse.class);
        String email = userInfoResponse.getBody().getEmail();
        String username = userInfoResponse.getBody().getName();

        System.out.println("User Info: " + username + ", " + email); // Debugging

        // 사용자 정보 데이터베이스에 저장
        Optional<Member> memberOptional = memberService.findByEmail(email);
        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
        } else {
            member = new Member();
            member.setUsername(username);
            member.setEmail(email);
            member.setPassword(""); // Google 로그인에서는 비밀번호가 필요 없음
            member.setRole("USER");
            memberService.saveMember(member);
        }

        // 사용자 인증 정보 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 클라이언트로 응답 반환
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("username", username);
        responseMap.put("id_token", jwtToken);
        return ResponseEntity.ok(responseMap);
    }
}
