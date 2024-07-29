package com.mysite.crawling.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysite.crawling.entity.Dog;
import com.mysite.crawling.service.DogCrawlingService;

@RestController
@RequestMapping("/api/dogs")
public class DogController {

    @Autowired
    private DogCrawlingService dogCrawlingService;


    // 모든 강아지 정보 반환
    @GetMapping("/all")
    public ResponseEntity<List<Dog>> getAllDogs() {
        List<Dog> dogs = dogCrawlingService.getAllDogs();
        return ResponseEntity.ok(dogs);
    }

    // 강아지 정보 크롤링 및 저장
    @PostMapping("/crawl")
    public ResponseEntity<String> crawlAndSaveDogs() {
        try {
            dogCrawlingService.crawlAndSaveDogs();
            return ResponseEntity.ok("Dogs crawled and saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while crawling dogs: " + e.getMessage());
        }
    }
}