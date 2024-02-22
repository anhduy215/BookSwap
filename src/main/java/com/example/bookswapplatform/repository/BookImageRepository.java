package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Book.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookImageRepository extends JpaRepository<BookImage, UUID> {
}
