package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostGeneralDTO {
    private UUID id;
    private String caption;
    private String exchangeMethod;
    private String city;
    private String district;
    private String locationDetail;
    private int numOfBook;
    private String imgUrl;
    private List<String> categories;
    private String createBy;
    private boolean deleted;
}
