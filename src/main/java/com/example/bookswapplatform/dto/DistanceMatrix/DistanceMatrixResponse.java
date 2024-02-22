package com.example.bookswapplatform.dto.DistanceMatrix;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceMatrixResponse {
    private String status;
    private List<Row> rows;
}
