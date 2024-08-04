package com.mysite.login.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class MemberResponse {
    private Long id;
    private String username;
    private String email;
    private String role; 

    // 기본 생성자
    public MemberResponse() {
    }

    // 생성자 (id, username, email, role을 받는 생성자)
    public MemberResponse(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
