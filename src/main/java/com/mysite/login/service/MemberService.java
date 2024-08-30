package com.mysite.login.service;

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
}