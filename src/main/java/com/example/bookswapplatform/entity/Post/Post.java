package com.example.bookswapplatform.entity.Post;

import com.example.bookswapplatform.common.ExchangeMethod;
import com.example.bookswapplatform.entity.Area.Area;
import com.example.bookswapplatform.entity.Area.District;
import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder

@Where(clause = "deleted=false")
public class Post {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private String caption;

    private String description;

    @Enumerated(EnumType.STRING)
    private ExchangeMethod exchangeMethod;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime updateDate;

    private String updateBy;

    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;

    @NotEmpty
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Book> books;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private PostStatus postStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;
    private String locationDetail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Orders> ordersSet;

    private boolean deleted = Boolean.FALSE;

    public void checkAndSetLockedStatus(PostStatus postStatus) {
        boolean allBooksLocked = books.stream().allMatch(Book::isLock);
        if (allBooksLocked) {
            setPostStatus(postStatus);
        }
    }


}
