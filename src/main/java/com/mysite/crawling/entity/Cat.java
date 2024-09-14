package com.mysite.crawling.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id") // 컬럼 이름을 'cat_id'로 지정
    private Long id;

    @Column(name = "cat_description") // 컬럼 이름을 'cat_description'으로 지정
    private String description;

    // 기본 생성자
    public Cat() {}

    // 매개변수가 있는 생성자
    public Cat(String description) {
        this.description = description;
    }

    // getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
