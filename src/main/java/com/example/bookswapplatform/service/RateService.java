package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.RateRequestDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface RateService {
    ResponseEntity<BaseResponseDTO> rateUser(Principal principal,UUID orderId, RateRequestDTO rateRequestDTO);

    ResponseEntity<BaseResponseDTO> viewRate(Principal principal);
    ResponseEntity<BaseResponseDTO> viewRateByCreateBy(Principal principal);
    ResponseEntity<BaseResponseDTO> viewOtherUserRate(UUID userId);
    ResponseEntity<BaseResponseDTO> viewOtherUserRateCreate(UUID userId);
}
