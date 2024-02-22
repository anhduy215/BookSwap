package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Post.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostStatusRepository extends JpaRepository<PostStatus, UUID> {
    Optional<PostStatus> findByName (String name);
}
