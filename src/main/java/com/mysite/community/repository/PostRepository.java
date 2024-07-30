package com.mysite.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.community.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Modifying
	@Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
	void incrementViews(@Param("id") Long id);
}
