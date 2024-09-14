package com.mysite.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.login.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	  Member findByEmail(String email);
	  Optional<Member> findByPasswordResetToken(String token);

}