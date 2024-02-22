package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.User.User;
import org.hibernate.annotations.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByTitle (String title);
    Optional<Book> findById (UUID id);
    @Query("SELECT b from Book b where b.createBy = :user and b.isDone = false and b.post is null and b.isLock = false ")
    Page<Book> findBookAvailable (User user, Pageable pageable);
    @Query("SELECT b from Book b where b.createBy = :user and b.isDone = true")
    Page<Book> findBookIsDone (User user, Pageable pageable);
    @Query("SELECT b from Book b where b.createBy = :user and b.post is not null")
    Page<Book> findBookInPost (User user, Pageable pageable);
    @Query("SELECT b from Book b where b.isDone = false and b.post is null and b.isLock = false ")
    Page<Book> findAllBookAvailable (Pageable pageable);
    @Query("SELECT b from Book b where b.isDone = true")
    Page<Book> findAllBookIsDone (Pageable pageable);
    @Query("SELECT b from Book b where b.post is not null")
    Page<Book> findAllBookInPost (Pageable pageable);
    @Query("SELECT b from Book b where b.createBy = :user and b.isDone = false and b.post is null")
    Set<Book> findByCreatedBy(User user);

//    Page<Book> findAll(Pageable pageable);
    @Query("SELECT DISTINCT b FROM Book b " +
            "JOIN b.authors a JOIN b.mainCategory mc " +
            "WHERE a.name like %:keyWord% or mc.name like %:keyWord% or b.publisher like %:keyWord% or " +
            "b.title like %:keyWord% or b.language like %:keyWord% or b.subCategory like %:keyWord% or b.subSubCategory like %:keyWord%   ")
    Page<Book> searchBookByKeyWord(String keyWord, Pageable pageable);



}
