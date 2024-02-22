package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface SavePostService {
    ResponseEntity<BaseResponseDTO> savePost (Principal principal, UUID postId);
    ResponseEntity<BaseResponseDTO> getSavePost (Principal principal);
    ResponseEntity<BaseResponseDTO> removePost (UUID postId);
}
