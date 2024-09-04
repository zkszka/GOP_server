package com.mysite.login.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendPasswordResetEmail(String to, String resetLink) {
        // 이메일 발송 로직 구현
        // 예: JavaMailSender를 사용하여 이메일 발송
        System.out.println("발송 이메일: " + to);
        System.out.println("비밀번호 리셋 링크: " + resetLink);
    }
}
