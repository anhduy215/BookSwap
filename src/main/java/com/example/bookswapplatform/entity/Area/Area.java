package com.example.bookswapplatform.entity.Area;

import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class Area {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private String city;

    @OneToMany(mappedBy = "city")
    private Set<District> districts;

    @OneToMany(mappedBy = "area")
    private Set<Post> posts;

    @OneToMany(mappedBy = "area")
    private Set<Orders> orders;

}
