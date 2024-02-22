package com.example.bookswapplatform.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateRequestDTO {
    @Min(value = 1, message = "Rate must be at least 1")
    @Max(value = 5, message = "Rate must be at most 5")
    private int rateNumber;
    @Nullable
    private String description;
}
