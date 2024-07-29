package com.mysite.crawling.controller;

import com.mysite.crawling.entity.Cat;
import com.mysite.crawling.service.CatCrawlingService;
import com.mysite.crawling.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class CatController {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private CatCrawlingService catCrawlingService;

    @PostMapping("/crawl")
    public ResponseEntity<String> crawlAndSaveCatInfo() {
        try {
            catCrawlingService.crawlAndSaveCatInfo();
            return ResponseEntity.ok("Cat information crawled and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while crawling cat information: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cat>> getAllCats() {
        List<Cat> cats = catRepository.findAll();
        return ResponseEntity.ok(cats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        Cat cat = catRepository.findById(id).orElse(null);
        if (cat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cat> updateCat(@PathVariable Long id, @RequestBody Cat updatedCat) {
        Cat existingCat = catRepository.findById(id).orElse(null);
        if (existingCat == null) {
            return ResponseEntity.notFound().build();
        }
        existingCat.setName(updatedCat.getName());
        existingCat.setDescription(updatedCat.getDescription());
        Cat savedCat = catRepository.save(existingCat);
        return ResponseEntity.ok(savedCat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCat(@PathVariable Long id) {
        Cat cat = catRepository.findById(id).orElse(null);
        if (cat == null) {
            return ResponseEntity.notFound().build();
        }
        catRepository.delete(cat);
        return ResponseEntity.ok("Deleted cat with id: " + id);
    }
}
