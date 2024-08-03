package com.mysite.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.community.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Modifying
	@Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
	void incrementViews(@Param("id") Long id);
	
	@Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Post> searchPosts(@Param("searchTerm") String searchTerm);
}
