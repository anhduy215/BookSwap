package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavePostRepository extends JpaRepository<SavedPost, UUID> {
    List<SavedPost> findAllByUserId(UUID userId);

    SavedPost findByPostId(UUID postId);
}
