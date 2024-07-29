package com.mysite.login.repository;

import com.mysite.login.entity.Member;

public interface MemberServiceImp {
	void saveMember(Member member);

	Member findByEmail(String email);

	Member login(String email, String password);

}
