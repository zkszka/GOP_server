package com.mysite.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.mysite.login.entity.Member;
import com.mysite.login.service.MemberService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final MemberService memberService;

    @Autowired
    public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/members/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.findMemberById(id);
        return member.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/members/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
        try {
            // 비밀번호와 이메일 필드를 무시하고 업데이트 처리
            memberService.updateMember(id, updatedMember);
            return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 업데이트 중 오류 발생");
        }
    }

    @DeleteMapping("/members/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok("회원이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 삭제 중 오류 발생");
        }
    }
}
