package com.mysite.login.repository;

import com.mysite.login.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    // 필요한 추가적인 메서드가 있다면 여기에 추가
}
