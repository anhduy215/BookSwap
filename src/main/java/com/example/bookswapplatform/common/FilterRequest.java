package com.example.bookswapplatform.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    //filter by date
    @Nullable
    private String createDate;
    //filter by area
    @Nullable
    private String city;
    @Nullable
    private String district;
    //filter by book's fields
    @Nullable
    private String category;
    @Nullable
    private String subCategory;
    @Nullable
    private String subSubCategory;
    @Nullable
    private String publisher;
    @Nullable
    private String publishedDate;
    @Nullable
    private String authors;
    @Nullable
    private String exchangeMethod;
    @Nullable
    private String language;

    public FilterRequest(String city) {
        this.city = city;
    }
}
