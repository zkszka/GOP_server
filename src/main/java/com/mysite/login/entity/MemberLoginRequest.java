package com.mysite.login.entity;

public class MemberLoginRequest {
    private String email;
    private String password;

    // 기본 생성자
    public MemberLoginRequest() {
    }

    // 생성자 (email, password 필드를 받아 초기화)
    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter, Setter 메서드
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}