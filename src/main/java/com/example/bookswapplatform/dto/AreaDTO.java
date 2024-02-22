package com.example.bookswapplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AreaDTO {
    private UUID id;
    private String city;
    private Set<DistrictDTO> districtDTOS;
}
