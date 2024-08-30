package com.mysite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GopServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GopServerApplication.class, args);
    }
}