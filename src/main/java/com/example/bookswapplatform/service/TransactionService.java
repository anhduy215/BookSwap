package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface TransactionService {
    ResponseEntity<BaseResponseDTO> getAllTransaction(Principal principal);
    ResponseEntity<BaseResponseDTO> getDetailTransaction(UUID id);
}
