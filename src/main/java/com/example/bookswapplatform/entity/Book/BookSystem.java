package com.example.bookswapplatform.entity.Book;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
public class  BookSystem {
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    private String subCategory;

    private String subSubCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_system_author", joinColumns = @JoinColumn(name = "book_system_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private Set<Author> authors;
}
