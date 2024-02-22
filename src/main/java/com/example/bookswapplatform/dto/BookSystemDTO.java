package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.entity.Book.Author;
import com.example.bookswapplatform.entity.Book.BookLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSystemDTO {
    private UUID id;
    private String title;
    private String description;
    private String publisher;
    private Integer year;
    private String isbn;
    private BookLanguage language;
    private int pageCount;
    private BigDecimal price;
    private String mainCategory;
    private String subCategory;
    private String subSubCategory;
    private Set<String> authors;
}
