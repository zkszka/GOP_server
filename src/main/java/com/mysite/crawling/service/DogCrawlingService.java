package com.mysite.crawling.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.crawling.entity.Dog;
import com.mysite.crawling.repository.DogRepository;

@Service
@Transactional
public class DogCrawlingService {

    @Autowired
    private DogRepository dogRepository;

    // 주기적으로 크롤링
    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void scrapeData() {
        String url = "https://m.dongascience.com/news.php?idx=15282";
        try {
            Document document = Jsoup.connect(url).get();
            Elements pElements = document.select("p");

            for (Element pElement : pElements) {
                String description = pElement.text();
                
                // 크롤링한 데이터를 출력하여 확인
                System.out.println("Crawled description: " + description);

                if (!description.trim().isEmpty()) { // 빈 값이 아닌 경우만 저장
                    // Dog 객체 생성 및 저장
                    Dog dog = new Dog(description);
                    dogRepository.save(dog);

                    System.out.println("Saved: " + description);
                } else {
                    System.out.println("Skipped empty description.");
                }
            }

            List<Dog> allDogs = dogRepository.findAll();
            System.out.println("All dogs: " + allDogs); // 모든 Dog 객체 출력
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리 및 로그 기록
        } catch (Exception e) {
            e.printStackTrace(); // 다른 예외 처리
        }
    }

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }
}
