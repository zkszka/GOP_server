package com.mysite.login.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysite.login.entity.Member;
import com.mysite.login.entity.MemberLoginRequest;
import com.mysite.login.entity.MemberResponse;
import com.mysite.login.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    public MemberController(MemberService memberService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody Member member) {
        try {
            if (member.getUsername() == null || member.getUsername().isEmpty() ||
                member.getEmail() == null || member.getEmail().isEmpty() ||
                member.getPassword() == null || member.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("필수 필드가 누락되었습니다.");
            }

            memberService.saveMember(member);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            logger.error("회원가입 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody MemberLoginRequest loginRequest) {
        try {
            // AuthenticationManager를 사용하여 인증
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // MemberService에서 Optional<Member>를 사용하여 사용자 찾기
            Optional<Member> memberOptional = memberService.findByEmail(loginRequest.getEmail());
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                MemberResponse memberResponse = new MemberResponse(
                    member.getId(),
                    member.getUsername(),
                    member.getEmail(),
                    member.getRole()
                );
                return ResponseEntity.ok(memberResponse);
            } else {
                logger.info("로그인 시도 실패: 사용자 없음. 이메일: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (BadCredentialsException e) {
            logger.info("로그인 시도 실패: 비밀번호 불일치. 이메일: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (AuthenticationException e) {
            logger.error("로그인 중 인증 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<MemberResponse> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 현재 로그인한 사용자의 이메일을 기반으로 사용자 찾기
            Optional<Member> memberOptional = memberService.findByEmail(authentication.getName());
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
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
    
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody MemberLoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            memberService.requestPasswordReset(email);
            return ResponseEntity.ok("비밀번호 재설정 이메일이 전송되었습니다.");
        } catch (Exception e) {
            logger.error("비밀번호 재설정 요청 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 요청 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
        @RequestParam String token, @RequestParam String newPassword) {
        try {
            boolean success = memberService.resetPassword(token, newPassword);
            if (success) {
                return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 재설정 실패: 유효하지 않은 토큰.");
            }
        } catch (Exception e) {
            logger.error("비밀번호 재설정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 중 오류가 발생했습니다.");
        }
    }
}
