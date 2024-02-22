package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<BaseResponseDTO> getAllCategory();
    ResponseEntity<BaseResponseDTO> getCategory();
    ResponseEntity<BaseResponseDTO> getSubCategory(String category);
    ResponseEntity<BaseResponseDTO> getSubSubCategory(String subCategory);
    ResponseEntity<BaseResponseDTO> getAllSubCategory();
    ResponseEntity<BaseResponseDTO> getAllSubSubCategory();
}
