package com.example.bookswapplatform.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @NotEmpty
    private String caption;
    private String description;
    @NotEmpty
    private String exchangeMethod;
    @NotEmpty
    private String city;
    @NotEmpty
    private String district;
    @NotEmpty
    private String locationDetail;
    @NotEmpty
    private List<BookPriceDTO> bookPriceDTOS;

}
