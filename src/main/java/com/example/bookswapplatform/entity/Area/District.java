package com.example.bookswapplatform.entity.Area;

import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
public class District {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private String district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private Area city;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private Set<Post> posts;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private Set<Orders> orders;
}
