package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private UUID id;
    private String caption;
    private String description;
    private String exchangeMethod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime updateDate;
    private String city;
    private String district;
    private String locationDetail;
    private String postStatus;
    private int views;
    private String createBy;
    private String updateBy;
    private UserGeneralDTO userGeneralDTO;
    private Set<BookDTO> bookDTOS;
}
