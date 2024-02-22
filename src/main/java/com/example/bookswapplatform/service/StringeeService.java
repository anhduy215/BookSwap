package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;

public interface StringeeService {
    ResponseEntity<BaseResponseDTO> generateAccessToken ();
}

