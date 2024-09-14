package com.mysite.community.entity;

import java.time.LocalDateTime;
import java.util.Base64;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "post_author")
    private String author;

    @Column(name = "post_createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "post_views")
    private int views = 0;

    @Column(name = "post_comments")
    private int comments = 0;

    @Lob
    @Column(name = "post_photo")
    private byte[] photo;

    // 기본 생성자
    public Post() {}

    // 매개변수가 있는 생성자
    public Post(String title, String content, String author, LocalDateTime createdAt, int views, int comments, byte[] photo) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.views = views;
        this.comments = comments;
        this.photo = photo;
    }

    // getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoUrl() {
        if (photo != null) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo);
        }
        return null;
    }
}
