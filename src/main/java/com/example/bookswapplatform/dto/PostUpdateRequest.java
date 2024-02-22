package com.example.bookswapplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class PostUpdateRequest {
    private String caption;
    private String description;
    @Schema(description = "If exchange method is trade/give give price value is 0 \n" +
            "If exchange method is sell user can give price value", example = "TRADE/GIVE/SELL")
    private String exchangeMethod;
    private String city;
    private String district;
//    @Schema(description = "If exchange method is trade price is 0, if sell user can input price")
//    private Set<BigDecimal> price;
//    @Schema(description = "Id book that user add price")
//    private Set<UUID> bookPriceId;

    private List<BookPriceDTO> bookPriceDTOS;
}
