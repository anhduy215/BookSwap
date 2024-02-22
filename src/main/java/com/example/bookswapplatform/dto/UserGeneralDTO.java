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
public class UserGeneralDTO {
    private UUID id;
    private String name;
    private float totalRate;
    private String imgUrl;
    private String fireBaseId;
    private String phone;
    private String email;
}
