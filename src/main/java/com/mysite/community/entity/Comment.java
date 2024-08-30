package com.mysite.community.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "comment") // 테이블 이름을 'comment'로 지정
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id") // 컬럼 이름을 'comment_id'로 지정
    private Long id;

    @Column(name = "comment_content", nullable = false) // 컬럼 이름을 'comment_content'로 지정
    private String content;

    @Column(name = "comment_author", nullable = false) // 컬럼 이름을 'comment_author'로 지정
    private String author;

    @Column(name = "comment_createdAt", nullable = false) // 컬럼 이름을 'comment_createdAt'로 지정
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false) // 컬럼 이름을 'post_id'로 지정
    private Post post;

    // 기본 생성자
    public Comment() {}

    // 매개변수가 있는 생성자
    public Comment(String content, String author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
