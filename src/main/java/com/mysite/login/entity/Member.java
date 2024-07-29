package com.mysite.login.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role = "USER"; // 기본 role 설정
    private Timestamp createDate = new Timestamp(System.currentTimeMillis()); // 현재 시간으로 설정

    public Member(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}