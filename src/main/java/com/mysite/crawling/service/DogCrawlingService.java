package com.mysite.crawling.service;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.crawling.entity.Dog;
import com.mysite.crawling.repository.DogRepository;

@Service
@Transactional
public class DogCrawlingService {

    @Autowired
    private DogRepository dogRepository;

    public void scrapeData() {
        String url = "https://m.dongascience.com/news.php?idx=15282";
        try {
            Document document = Jsoup.connect(url).get();
            Elements pElements = document.select("p");

            for (Element pElement : pElements) {
                String description = pElement.text().trim();

                System.out.println("Crawled description: " + description);

                if (!description.isEmpty()) {
                    // Dog 객체 생성 및 저장
                    Dog dog = new Dog(description);
                    dogRepository.save(dog);
                    System.out.println("Saved: " + description);
                } else {
                    System.out.println("Skipped empty description.");
                }
            }

            List<Dog> allDogs = dogRepository.findAll();
            System.out.println("All dogs: " + allDogs);
        } catch (IOException e) {
            System.err.println("Error connecting to the URL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }
}
