package com.mysite.login.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.login.entity.Member;
import com.mysite.login.repository.MemberRepository;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    // 이메일을 기반으로 사용자 검색
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberRepository.findByEmail(email));
    }

    // 비밀번호 일치 여부 검사
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    // 회원 저장
    public void saveMember(Member member) {
        // 비밀번호 암호화하여 저장
        String encryptedPassword = bCryptPasswordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);
        memberRepository.save(member);
    }

    // 로그인 처리
    public Optional<Member> login(String email, String rawPassword) {
        Optional<Member> memberOpt = findByEmail(email);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            boolean passwordMatch = matchesPassword(rawPassword, member.getPassword());
            logger.info("Login attempt: Email: {}, Password matches: {}", email, passwordMatch);
            if (passwordMatch) {
                return Optional.of(member);
            }
        }
        logger.info("Login failed: Email: {}", email);
        return Optional.empty(); // 로그인 실패
    }

    // 모든 회원 목록 조회
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 ID로 조회
    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // 회원 정보 업데이트
    public void updateMember(Long id, Member updatedMember) {
        if (memberRepository.existsById(id)) {
            Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
            
            // 비밀번호와 이메일은 업데이트하지 않음
            if (updatedMember.getUsername() != null) {
                existingMember.setUsername(updatedMember.getUsername());
            }
            if (updatedMember.getRole() != null) {
                existingMember.setRole(updatedMember.getRole());
            }

            // 비밀번호는 암호화된 상태로 유지
            memberRepository.save(existingMember);
        }
    }

    // 회원 삭제
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    
    // 비밀번호 재설정 요청을 위한 토큰 생성 및 이메일 발송
    public void requestPasswordReset(String email) {
        Optional<Member> memberOpt = findByEmail(email);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            // 비밀번호 재설정 토큰 생성
            String token = UUID.randomUUID().toString();
            member.setPasswordResetToken(token);
            // 토큰 만료 시간을 현재 시간 + 1시간으로 설정 (예시)
            member.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
            memberRepository.save(member);

            // 이메일 발송
            emailService.sendPasswordResetEmail(email, token);
            logger.info("비밀번호 재설정 이메일을 전송했습니다. 이메일: {}", email);
        } else {
            logger.warn("비밀번호 재설정 요청 실패: 이메일이 존재하지 않음. 이메일: {}", email);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<Member> memberOptional = memberRepository.findByPasswordResetToken(token);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            // 토큰 만료 시간 확인
            if (member.getPasswordResetTokenExpiry().isAfter(LocalDateTime.now())) {
                String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
                member.setPassword(encryptedPassword);
                member.setPasswordResetToken(null); // 토큰 사용 후 삭제
                member.setPasswordResetTokenExpiry(null); // 만료 시간 삭제
                memberRepository.save(member);
                return true;
            } else {
                logger.warn("비밀번호 재설정 실패: 토큰이 만료되었습니다. 토큰: {}", token);
            }
        }
        return false;
    }
}
