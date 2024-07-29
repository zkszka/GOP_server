package com.mysite.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.community.entity.Post;
import com.mysite.community.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post updatedPost) {
        // 예시 코드로, 실제 필요에 따라 수정이 필요할 수 있습니다.
        return postRepository.save(updatedPost);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}