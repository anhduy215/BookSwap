package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String lastName;
    private String firstName;
    private String phoneNum;
    private String idCard;
    private String email;
    private String fireBaseId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate dateOfBirth;
    private String gender;
    private String image;
    private float totalRate;
    private int numOfRate;
    private UserWalletDTO userWalletDTO;
}
