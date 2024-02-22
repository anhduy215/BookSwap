package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoriesDTO {
    private UUID id;
    private String name;
    private List<SubSubCategoriesDTO> subSubCategoriesDTOS;

}
