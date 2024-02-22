package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String lastName;
    private String firstName;
    @Size(min = 10, max = 12, message = "Phone number must be between 10 and 12 digits")
    @Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
    private String phoneNum;
    @Size(min = 12, max = 12, message = "Id card must be 12 digits")
    @Pattern(regexp = "\\d+", message = "Id card must contain only digits")
    private String idCard;
    private String image;
    @Schema(description = "MALE/FEMALE")
    private String gender;
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Date of birth must be in the format dd-MM-yyyy")
    private String dateOfBirth;
}
