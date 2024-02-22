package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderGeneralDTO {
    private UUID id;
    private BigDecimal price;
    private UserOrderDTO userOrderDTO;
    private String cancelBy;
    private PostGeneralDTO postGeneralDTO;
    private Set<BookGeneralDTO> bookGeneralDTOS;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;
    private boolean isPayment;
    private boolean isConfirm;

}
