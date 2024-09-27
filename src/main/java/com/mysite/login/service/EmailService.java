package com.mysite.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tenandten10@naver.com"); 
        message.setTo(to);
        message.setSubject("GOP 비밀번호 재설정 요청");
        message.setText("비밀번호 재설정을 위해 아래 링크를 클릭하세요:\n" +
        		"http://web-gopprj-m128ei6pc6510b38.sel4.cloudtype.app/login/new_pw?token=" + token);
        javaMailSender.send(message);
    }
}
