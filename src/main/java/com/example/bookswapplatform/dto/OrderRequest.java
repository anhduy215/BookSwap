package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.common.ExchangeMethod;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
//    @NotEmpty
//    @Valid
//    private List<BookTradeId> bookTradeIds;

    @NotEmpty
    private List<UUID> bookIdInPosts;
    private List<UUID> bookIdOfUsers;

    private String note;

    @Min(value = 0, message = "Input 0 or 1")
    @Max(value = 1, message = "Input 0 or 1")
    private int isShip;
    @Nullable
    private String percentPay;
    @Nullable
    private String city;
    @Nullable
    private String district;
    private String locationDetail;
    public String getPercentPay() {
        return percentPay;
    }

    public void setPercentPay(String percentPay) {
        // Validate that percentPay is one of the allowed values
        if (isValidPercentPay(percentPay)) {
            this.percentPay = percentPay;
        } else {
            // You can throw an exception, log an error, or handle the validation failure in some way.
            throw new IllegalArgumentException("Invalid percentPay value must be 25/50/75/100");
        }
    }

    // Validation method to check if percentPay is one of the allowed values
    private boolean isValidPercentPay(String percentPay) {
        return percentPay == null || (percentPay.equals("25") || percentPay.equals("50") ||
                percentPay.equals("75") || percentPay.equals("100"));
    }

    public boolean isTradeOrderValid(ExchangeMethod exchangeMethod) {
        if (bookIdInPosts == null) {
            // Handle the case where bookTradeIds is null
            return false;
        }
        if (exchangeMethodIsTrade(exchangeMethod) && bookIdOfUsers == null) {
            return false;
        }
        return exchangeMethodIsTrade(exchangeMethod) || bookIdOfUsers == null;
    }

    // Assume you have a method to get the exchange method, adjust as needed
    private boolean exchangeMethodIsTrade(ExchangeMethod exchangeMethod) {
        return exchangeMethod.equals(ExchangeMethod.TRADE);
    }

    public List<UUID> getBookTradeIds() {
        List<UUID> result = new ArrayList<>();

        if (bookIdInPosts != null) {
            result.addAll(bookIdInPosts);
        }

        if (bookIdOfUsers != null) {
            result.addAll(bookIdOfUsers);
        }

        return result;
    }
}
