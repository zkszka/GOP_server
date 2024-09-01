package com.mysite.login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.login.entity.Member;
import com.mysite.login.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 이메일을 기반으로 사용자 검색
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
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
    public Member login(String email, String rawPassword) {
        Member member = findByEmail(email);

        if (member != null) {
            boolean passwordMatch = matchesPassword(rawPassword, member.getPassword());
            logger.info("Login attempt: Email: {}, Password matches: {}", email, passwordMatch);
            if (passwordMatch) {
                return member;
            }
        }
        logger.info("Login failed: Email: {}", email);
        return null; // 로그인 실패
    }

    // 모든 회원 목록 조회
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 ID로 조회
    public Member findMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElse(null);
    }

    // 회원 정보 업데이트
    public void updateMember(Long id, Member updatedMember) {
        if (memberRepository.existsById(id)) {
            Member existingMember = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
            
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
}
