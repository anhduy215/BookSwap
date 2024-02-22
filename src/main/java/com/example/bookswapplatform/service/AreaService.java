package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;

public interface AreaService {
    ResponseEntity<BaseResponseDTO> getAllArea ();
    ResponseEntity<BaseResponseDTO> getCity();
    ResponseEntity<BaseResponseDTO> getDistrict(String city);
    ResponseEntity<BaseResponseDTO> getAllDistrict();
}
