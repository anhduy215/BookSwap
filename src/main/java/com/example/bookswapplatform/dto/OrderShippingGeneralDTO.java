package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippingGeneralDTO {
    private PostGeneralDTO postGeneralDTO;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;
    private Set<OrderShippingDTO> orderShippingDTOS;
}
