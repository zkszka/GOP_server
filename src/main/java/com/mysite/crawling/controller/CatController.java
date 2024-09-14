package com.mysite.crawling.controller;

import com.mysite.crawling.entity.Cat;
import com.mysite.crawling.service.CatCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class CatController {

    @Autowired
    private CatCrawlingService catCrawlingService;

    @PostMapping("/crawl")
    public ResponseEntity<String> crawlAndSaveCatInfo() {
        try {
            // CatCrawlingService의 scrapeData 메소드를 호출하여 데이터를 크롤링하고 저장
            catCrawlingService.scrapeData();
            return ResponseEntity.ok("Cat information crawled and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while crawling cat information: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cat>> getAllCats() {
        try {
            // 모든 Cat 객체를 조회
            List<Cat> cats = catCrawlingService.getAllCats();
            return ResponseEntity.ok(cats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
