package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderDTO {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String city;
    private String district;
    private String fireBaseId;
    private String imgUrl;
}
