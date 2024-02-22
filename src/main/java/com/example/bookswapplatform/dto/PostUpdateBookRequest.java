package com.example.bookswapplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateBookRequest {
    private UUID oldBookId;
    private UUID newBookId;
    @Schema(description = "Delete - delete book in post \n" +
            "Add - add book", example = "Delete/Add")
    private String updateMethod;
}
