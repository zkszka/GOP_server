package com.mysite.login.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role = "USER"; // 기본 role 설정
    private LocalDateTime createDate = LocalDateTime.now(); // 현재 시간으로 설정

    // Lombok은 기본 생성자, 모든 필드를 포함한 생성자, getter, setter를 자동으로 생성합니다.
}
