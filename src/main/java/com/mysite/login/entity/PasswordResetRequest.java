package com.mysite.login.entity;

public class PasswordResetRequest {
    private String email;
    private String password; // 새로운 비밀번호

    // Getter 및 Setter
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
