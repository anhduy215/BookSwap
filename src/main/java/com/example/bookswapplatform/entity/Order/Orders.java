package com.example.bookswapplatform.entity.Order;

import com.example.bookswapplatform.entity.Area.Area;
import com.example.bookswapplatform.entity.Area.District;
import com.example.bookswapplatform.entity.Payment.Payment;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.User.Rate;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class Orders {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private BigDecimal senderPrice;
    private BigDecimal receiverPrice;
    private BigDecimal bookPrice;
    private BigDecimal senderShipPrice;
    private BigDecimal receiverShipPrice;
    private BigDecimal fee;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    private String locationDetail;

    @Column(columnDefinition = "boolean")
    @ColumnDefault("false")
    private boolean isConfirm;

    @Column(columnDefinition = "boolean")
    @ColumnDefault("false")
    private boolean isPayment;

    @Column(columnDefinition = "boolean")
    @ColumnDefault("false")
    private boolean isShipping;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime autoRejectTime;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;
    private String userCancel;

    private String updateBy;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "orders" ,cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @OneToOne(mappedBy = "orders")
    private Rate rate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private Set<CancelOrderHistory> cancellationHistories;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL )
    private Set<OrderShipping> shippingOrders;
}
