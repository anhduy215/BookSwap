package com.example.bookswapplatform.entity.Book;

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
public class Author {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;
    @ManyToMany(mappedBy = "authors")
    private Set<BookSystem> bookSystems;
}
