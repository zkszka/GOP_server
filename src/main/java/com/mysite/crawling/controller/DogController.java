package com.mysite.crawling.controller;

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
        if (dogs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(dogs);
    }

    // 강아지 정보 크롤링 및 저장
    @PostMapping("/crawl")
    public ResponseEntity<String> crawlAndSaveDogs() {
        try {
            dogCrawlingService.scrapeData(); // 메서드 호출 이름 수정
            return new ResponseEntity<>("크롤링 및 데이터 저장이 완료되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("크롤링 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
