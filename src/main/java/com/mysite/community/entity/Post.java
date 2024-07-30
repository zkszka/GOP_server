package com.mysite.community.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int views; 
    private int comments;
    private String role;

    // 생성자, getter, setter 등 필요한 코드 작성

    public Post() {
        this.createdAt = LocalDateTime.now();
        this.views = 0;
        this.comments = 0;
    }

}
