package com.example.bookswapplatform.dto.DistanceMatrix;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Element {
    private Distance distance;
    private Duration duration;
    private String status;
}
