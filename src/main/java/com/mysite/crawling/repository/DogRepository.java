package com.mysite.crawling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.crawling.entity.Dog;

public interface DogRepository extends JpaRepository<Dog, Long> {
}