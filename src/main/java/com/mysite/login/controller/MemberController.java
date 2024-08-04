package com.mysite.login.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysite.login.entity.Member;
import com.mysite.login.entity.MemberLoginRequest;
import com.mysite.login.entity.MemberResponse;
import com.mysite.login.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    public MemberController(MemberService memberService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberService = memberService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody Member member) {
        try {
            // 비밀번호 암호화하여 저장
            String encryptedPassword = bCryptPasswordEncoder.encode(member.getPassword());
            member.setPassword(encryptedPassword);
            
            memberService.saveMember(member);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody MemberLoginRequest loginRequest, HttpServletRequest request) {
        // 하드코딩된 이메일과 비밀번호로 인증
        String hardcodedEmail = "norri1014@naver.com";
        String hardcodedPassword = "norriGOP1234";

        if (loginRequest.getEmail().equals(hardcodedEmail) && loginRequest.getPassword().equals(hardcodedPassword)) {
            HttpSession session = request.getSession(true);
            System.out.println("Session created: " + session.getId());

            // 인증된 사용자로 설정 (권한은 ADMIN으로 설정)
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), null, new ArrayList<>())
            );

            // 회원 응답 생성
            MemberResponse memberResponse = new MemberResponse(null, "운영자", loginRequest.getEmail(), "ADMIN");
            return ResponseEntity.ok(memberResponse);
        } else {
            Member member = memberService.findByEmail(loginRequest.getEmail());
            if (member != null && bCryptPasswordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
                HttpSession session = request.getSession(true);
                System.out.println("Session created: " + session.getId());
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(member.getEmail(), null, new ArrayList<>())
                );
                MemberResponse memberResponse = new MemberResponse(member.getId(), member.getUsername(), member.getEmail(), member.getRole());
                return ResponseEntity.ok(memberResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }


    
    @GetMapping("/check-session")
    public ResponseEntity<MemberResponse> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Member member = memberService.findByEmail(authentication.getName());
            if (member != null) {
                // Role을 추가하여 MemberResponse 객체 생성
                MemberResponse memberResponse = new MemberResponse(member.getId(), member.getUsername(), member.getEmail(), member.getRole());
                return ResponseEntity.ok(memberResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃 실패: 인증되지 않음");
        }
    }
}
