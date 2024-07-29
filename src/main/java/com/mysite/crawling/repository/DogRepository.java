package com.mysite.crawling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.crawling.entity.Dog;

public interface DogRepository extends JpaRepository<Dog, Long> {
    // 특별히 추가할 메서드가 없으면 기본 CRUD 기능을 JpaRepository에서 제공받습니다.
}