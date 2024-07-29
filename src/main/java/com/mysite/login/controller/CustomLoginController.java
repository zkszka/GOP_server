package com.mysite.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomLoginController  {

    @GetMapping("/custom-login")
    public String showCustomLoginPage() {
        return "custom-login"; // 커스텀 로그인 페이지의 이름 (예: custom-login.html, custom-login.jsp 등)
    }
}
