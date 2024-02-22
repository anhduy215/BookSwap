package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookGeneralDTO {
    private UUID id;
    private String title;
    private String coverImg;
    private String createBy;
    private BigDecimal price;
    private String category;
}
