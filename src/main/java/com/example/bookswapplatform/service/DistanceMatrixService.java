package com.example.bookswapplatform.service;

import java.util.Map;

public interface DistanceMatrixService {
    Map<String, String> getDistanceMatrix(String origins, String destinations);
}
