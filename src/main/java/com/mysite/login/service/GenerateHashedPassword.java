package com.mysite.login.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHashedPassword {
	 public static void main(String[] args) {
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String plainPassword = "asd123";
	        String hashedPassword = encoder.encode(plainPassword);
	        System.out.println("Hashed Password: " + hashedPassword);
	    }

}
