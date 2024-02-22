package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.NotificationRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface NotificationService {
    ResponseEntity<BaseResponseDTO> save (Principal principal, NotificationRequest notificationRequest, UUID userId);
    ResponseEntity<BaseResponseDTO> getAll (Principal principal);
}
