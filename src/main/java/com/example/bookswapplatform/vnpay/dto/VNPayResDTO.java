package com.example.bookswapplatform.vnpay.dto;

import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPayResDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime timestamp;
    private String status;
    private String message;
    private String url;
}
