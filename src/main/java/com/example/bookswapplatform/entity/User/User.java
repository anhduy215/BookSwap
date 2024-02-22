package com.example.bookswapplatform.entity.User;

import com.example.bookswapplatform.common.Gender;
import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.Notification;
import com.example.bookswapplatform.entity.Order.CancelOrderHistory;
import com.example.bookswapplatform.entity.Order.OrderShipping;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Payment.UserWallet;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.Role.Role;
import com.example.bookswapplatform.entity.SystemLog.SystemLog;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
public class User implements UserDetails {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private String lastName;

    private String firstName;

    @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 digits")
    @Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
    private String phone;

    @Size(min = 12, max = 12, message = "Id card must be 12 digits")
    @Pattern(regexp = "\\d+", message = "Id card must contain only digits")
    private String idCard;

    @Column(unique = true)
    @Email
    private String email;

    private String passWord;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String city;
    private String district;
    private String locationDetail;

    @Column(columnDefinition = "boolean")
    @ColumnDefault("true")
    private boolean isEnable;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime updateDate;

    private String updateBy;

    private String image;

    private String provider;

    private String fireBaseUid;

    @Column(columnDefinition = "float")
    @ColumnDefault("0")
    private float totalRate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "createBy")
    private List<Post> postList;

    @OneToMany(mappedBy = "user")
    private List<Rate> rates;

    @OneToMany(mappedBy = "createBy")
    private List<Rate> rateCreated;

    @OneToMany(mappedBy = "createBy")
    private List<Orders> ordersList;

    @OneToMany(mappedBy = "createBy")
    private List<Book> bookList;

    @OneToOne(mappedBy = "createBy")
    private UserWallet userWallet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CancelOrderHistory> cancellationHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<OrderShipping> shippingOrders;

    @OneToMany(mappedBy = "user")
    private Set<SystemLog> systemLogs;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthor();
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
