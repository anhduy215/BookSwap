package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Book.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorsRepository extends JpaRepository<Author, UUID> {
    Optional<Author> findByName(String name);
}
