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

import com.mysite.crawling.entity.Cat;
import com.mysite.crawling.repository.CatRepository;

@Service
public class CatCrawlingService {

    @Autowired
    private CatRepository catRepository;

    @Transactional
    public void crawlAndSaveCatInfo() throws IOException {
        // 크롤링할 페이지 URL
        String url = "https://blog.idbins.com/341";

        // Jsoup을 사용하여 페이지를 가져옴
        Document doc = Jsoup.connect(url).get();

        // 공백이 없는 p 태그를 가져오기
        Elements pTags = doc.select("p");

        // p 태그의 텍스트를 가져오고, 공백이 없는 경우에만 포함
        StringBuilder contentBuilder = new StringBuilder();
        for (Element pTag : pTags) {
            String text = pTag.text().trim();
            if (!text.isEmpty()) {
                contentBuilder.append(text).append("\n");
            }
        }
        String content = contentBuilder.toString();

        // 크롤링한 데이터를 출력하여 확인
        System.out.println("Crawled content: \n" + content);

        // Cat 엔티티 생성 및 저장
        Cat cat = new Cat();
        cat.setDescription(content); // content를 description으로 설정
        catRepository.save(cat);

        // 저장된 Cat 객체 확인
        List<Cat> allCats = catRepository.findAll();
        System.out.println("All cats in database: " + allCats); // 모든 Cat 객체 출력
    }
}
