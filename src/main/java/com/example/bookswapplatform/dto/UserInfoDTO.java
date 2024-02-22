package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserInfoDTO {
    private String lastName;
    private String firstName;
    private String phoneNum;
    private String email;
    private String image;
    private String fireBaseId;
    private float totalRate;
    private int numOfRate;
    private int numOfPost;
    private List<PostGeneralDTO> postGeneralDTOs;
}
