package com.mysite.crawling.service;

import java.io.IOException;
import java.util.ArrayList;
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

    public void crawlAndSaveDogs() throws IOException {
        // 강아지 정보가 있는 웹페이지 URL
        String url = "https://www.mhns.co.kr/news/articleView.html?idxno=504693";

        // Jsoup을 사용하여 웹페이지에서 강아지 정보를 크롤링
        Document doc = Jsoup.connect(url).get();
        Elements dogParagraphs  = doc.select("p");

        List<Dog> dogs = new ArrayList<>();

        for (Element paragraph : dogParagraphs) {
            String description = paragraph.text().trim(); // p 태그 안의 텍스트 추출 및 공백 제거
            
            // 강아지 이름은 예시로 고정된 값을 넣습니다.
            String name = "테스트 강아지 이름"; 
            
            Dog dog = new Dog(name, description);
            dogs.add(dog);
        }

        // 크롤링한 강아지 정보를 데이터베이스에 저장
        dogRepository.saveAll(dogs);
    }

    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }
}
