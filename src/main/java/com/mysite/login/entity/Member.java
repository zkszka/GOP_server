package com.mysite.login.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member") // 테이블 이름 지정
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id") // 컬럼 이름 지정
    private Long id;

    @Column(name = "member_name") // 컬럼 이름 지정
    private String username;

    @Column(name = "member_email") // 컬럼 이름 지정
    private String email;

    @Column(name = "member_password") // 컬럼 이름 지정
    private String password;

    @Column(name = "member_role") // 컬럼 이름 지정
    private String role = "USER"; // 기본 role 설정

    @Column(name = "member_createdAt") // 컬럼 이름 지정
    private LocalDateTime createDate = LocalDateTime.now(); // 현재 시간으로 설정

    // 기본 생성자
    public Member() {}

    // 매개변수가 있는 생성자
    public Member(String username, String email, String password, String role, LocalDateTime createDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createDate = createDate;
    }

    // getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
