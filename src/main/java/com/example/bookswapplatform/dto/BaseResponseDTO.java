package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.common.Pagination;
import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponseDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private Pagination pagination;
    private Object data;

    public BaseResponseDTO(LocalDateTime timestamp, HttpStatus status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
