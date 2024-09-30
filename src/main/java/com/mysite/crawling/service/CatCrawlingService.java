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

import com.mysite.crawling.entity.Cat;
import com.mysite.crawling.repository.CatRepository;

@Service
@Transactional
public class CatCrawlingService {

    @Autowired
    private CatRepository catRepository;

    public void scrapeData() {
        String url = "https://univ20.com/12774";
        try {
            Document document = Jsoup.connect(url).get();
            Elements h3Elements = document.select("h3");
            Elements pElements = document.select("p");

            for (Element h3Element : h3Elements) {
                String title = h3Element.text();
                if (!title.trim().isEmpty()) {
                    // h3 태그의 텍스트를 description으로 저장
                    Cat cat = new Cat(title);
                    catRepository.save(cat);
                    System.out.println("Saved h3: " + title);
                } else {
                    System.out.println("Skipped empty h3.");
                }
            }

            for (Element pElement : pElements) {
                String description = pElement.text();
                if (!description.trim().isEmpty()) {
                    // p 태그의 텍스트를 description으로 저장
                    Cat cat = new Cat(description);
                    catRepository.save(cat);
                    System.out.println("Saved p: " + description);
                } else {
                    System.out.println("Skipped empty p.");
                }
            }

            List<Cat> allCats = catRepository.findAll();
            System.out.println("All cats: " + allCats); // 모든 Cat 객체 출력
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리 및 로그 기록
        } catch (Exception e) {
            e.printStackTrace(); // 다른 예외 처리
        }
    }

    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }
}
