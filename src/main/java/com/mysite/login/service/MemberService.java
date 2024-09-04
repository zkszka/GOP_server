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
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Map<String, String> resetTokens = new HashMap<>(); // 메모리 기반 토큰 저장 (실제 환경에서는 DB 사용)

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public void saveMember(Member member) {
        String encryptedPassword = bCryptPasswordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);
        memberRepository.save(member);
    }

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

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Member findMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElse(null);
    }

    public void updateMember(Long id, Member updatedMember) {
        if (memberRepository.existsById(id)) {
            Member existingMember = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
            
            if (updatedMember.getUsername() != null) {
                existingMember.setUsername(updatedMember.getUsername());
            }
            if (updatedMember.getRole() != null) {
                existingMember.setRole(updatedMember.getRole());
            }

            memberRepository.save(existingMember);
        }
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public void savePasswordResetToken(String email, String token) {
        resetTokens.put(token, email);
    }

    public boolean resetPassword(String token, String newPassword) {
        String email = resetTokens.get(token);
        if (email == null) {
            return false;
        }
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            member.setPassword(bCryptPasswordEncoder.encode(newPassword));
            memberRepository.save(member);
            resetTokens.remove(token);
            return true;
        }
        return false;
    }
}
