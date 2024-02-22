package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.OrderFilterRequest;
import com.example.bookswapplatform.dto.UserFilterRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<BaseResponseDTO> orderFilter(int pageNumber,
                                                int pageSize,
                                                String sortBy,
                                                String sortOrder,
                                                String keyWord,
                                                String status);

    ResponseEntity<BaseResponseDTO> userFilter(int pageNumber,
                                               int pageSize,
                                               String sortBy,
                                               String sortOrder, String keyWord,
                                               String role);
}
