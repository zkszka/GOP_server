package com.mysite.crawling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.crawling.entity.Cat;

public interface CatRepository extends JpaRepository<Cat, Long> {

}