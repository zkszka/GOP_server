package com.mysite.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.community.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostId(Long postId);
}