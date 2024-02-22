package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavePostDTO {
    private List<PostGeneralDTO> postGeneralDTOs;
}
