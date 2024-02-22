package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Book.BookSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookSystemRepository extends JpaRepository<BookSystem, UUID> {
    @Query("SELECT b FROM BookSystem b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<BookSystem> findByTitleContaining(@Param("searchTerm") String searchTerm);
}
