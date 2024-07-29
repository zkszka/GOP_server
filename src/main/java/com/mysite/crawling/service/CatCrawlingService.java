package com.mysite.crawling.service;

import java.io.IOException;

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
        String url = "https://www.sedaily.com/NewsView/1RZFDGQ7XQ";

        // Jsoup을 사용하여 페이지를 가져옴
        Document doc = Jsoup.connect(url).get();

        // article-view 클래스를 가진 모든 div 요소 가져오기
        Elements articleViews = doc.select(".article_view");

        // 기사의 제목 가져오기
        String title = doc.title();

        // 기사의 내용 가져오기 (article-view 클래스를 가진 div 안에 있는 모든 텍스트를 가져옴)
        StringBuilder contentBuilder = new StringBuilder();
        for (Element articleView : articleViews) {
            contentBuilder.append(articleView.text()).append("\n");
        }
        String content = contentBuilder.toString();

        // Cat 엔티티 생성 및 저장
        Cat cat = new Cat();
        cat.setName(title); // 기존 title을 name으로 변경
        cat.setDescription(content); // 기존 content를 description으로 변경
        catRepository.save(cat);
    }
}
