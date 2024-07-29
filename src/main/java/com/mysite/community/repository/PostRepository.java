package com.mysite.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.community.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
