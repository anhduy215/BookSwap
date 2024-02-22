package com.example.bookswapplatform.entity.Book;

import com.example.bookswapplatform.entity.Order.OrderDetail;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder

@Where(clause = "deleted=false")
public class Book {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private String title;

    private String description;

    private String publisher;

    @Digits(integer = 4, fraction = 0, message = "Please enter a valid year")
    @Range(min = 1900, max = 2100, message = "Year must be between 1900 and 2100")
    private Integer year;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookLanguage language;

    private int pageCount;

    private BigDecimal price;

    @Size(min = 1, max = 3, message = "New percent only in 0 and 100%")
    @Pattern(regexp = "\\d+", message = "New percent must contain only digits")
    private String newPercent;

    @ColumnDefault("false")
    private boolean isDone;

    @ColumnDefault("false")
    private boolean isLock;

    private boolean deleted = Boolean.FALSE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private Set<Author> authors;

    @OneToMany(mappedBy = "book")
    private Set<BookImage> bookImages;

    private String coverImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    private String subCategory;

    private String subSubCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;

    private String updateBy;



}
