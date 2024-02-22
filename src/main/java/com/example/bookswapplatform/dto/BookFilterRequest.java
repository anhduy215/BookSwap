package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookFilterRequest {
    private String language;
    private String mainCategory;
    private String subCategory;
    private String subSubCategory;
}
