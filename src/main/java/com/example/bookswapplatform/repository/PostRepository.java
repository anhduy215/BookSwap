package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.Post.PostStatus;
import com.example.bookswapplatform.entity.User.User;
import org.hibernate.annotations.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByCreateByAndPostStatus(User user, PostStatus postStatus);
    @Query("SELECT p FROM Post p JOIN p.createBy u WHERE p.postStatus.name = 'ACTIVE' OR p.postStatus.name = 'LOCKED' AND u.role.name = 'USER'")
    Page<Post> findAllNotDeactive(Pageable pageable);

//    @Query("SELECT DISTINCT p FROM Post p " +
//            "JOIN p.books b JOIN b.authors a " +
//            "WHERE p.postStatus.name = 'ACTIVE' AND (a.name like %:keyWord% or concat(b.title, ' ', b.description, ' ', b.publisher, ' ', " +
//            "b.mainCategory.name, ' ', b.subCategory.name, ' ', b.subSubCategory.name, ' ', b.isbn) like %:keyWord% or " +
//            "concat(p.caption, ' ', p.description, ' ', p.area.city, ' ', p.district.district) like %:keyWord%)")
//    Page<Post> searchPostsByKeyword(@Param("keyWord") String keyWord, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.books b JOIN b.authors a " +
            "WHERE p.postStatus.name = 'ACTIVE' AND " +
            "(p.caption like %:keyWord% or p.description like %:keyWord% or " +
            "p.area.city like %:keyWord% or p.district.district like %:keyWord% or " +
            "b.title like %:keyWord% or b.description like %:keyWord% or b.publisher like %:keyWord% or " +
            "b.isbn like %:keyWord% or " +
            "b.mainCategory.name like %:keyWord% or b.subCategory like %:keyWord% or " +
            "b.subSubCategory like %:keyWord%  or a.name like %:keyWord% )")
    Page<Post> searchPostsByKeyword(@Param("keyWord") String keyWord, Pageable pageable);


    @Query("SELECT p FROM Post p JOIN p.books b JOIN b.authors a " +
            "WHERE p.postStatus.name = 'ACTIVE' AND (:createDate IS NULL OR p.createDate = :createDate) " +
            "AND (:city IS NULL OR p.area.city = :city) " +
            "AND (:district IS NULL OR p.district = :district) " +
            "AND (:category IS NULL OR b.mainCategory.name = :category) " +
            "AND (:subCategory IS NULL OR b.subCategory = :subCategory) " +
            "AND (:subSubCategory IS NULL OR b.subSubCategory = :subSubCategory) " +
            "AND (:publisher IS NULL OR b.publisher = :publisher) " +
            "AND (:year IS NULL OR b.year = :year) " +
            "AND (:authors IS NULL OR a.name = :authors) " +
            "AND (:exchangeMethod IS NULL OR p.exchangeMethod = :exchangeMethod) " +
            "AND (:language IS NULL OR b.language = :language)")
    Page<Post> searchPostByFilter(@Param("createDate") String createDate,
                                  @Param("city") String city,
                                  @Param("district") String district,
                                  @Param("category") String category,
                                  @Param("subCategory") String subCategory,
                                  @Param("subSubCategory") String subSubCategory,
                                  @Param("publisher") String publisher,
                                  @Param("year") Integer year,
                                  @Param("authors") String authors,
                                  @Param("exchangeMethod") String exchangeMethod,
                                  @Param("language") String language, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p JOIN p.ordersSet o WHERE o = :orders")
    Post getPostByOrder(@Param("orders") Orders orders);
    @Query(value = "SELECT * FROM post WHERE id = :uuid", nativeQuery = true)
    Optional<Post> findIncludeDeletedPost(@Param("uuid") UUID uuid);



}
