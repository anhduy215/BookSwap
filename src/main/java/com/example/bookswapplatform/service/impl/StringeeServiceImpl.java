package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.StringeeService;
import com.example.bookswapplatform.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StringeeServiceImpl implements StringeeService {
    @Override
    public ResponseEntity<BaseResponseDTO> generateAccessToken() {
        String apiKeySid = "SK.0.VUwQbY27F9DqtjdQAKcKEahrklq6eous";
        String apiKeySecret = "VlBuUXIyaWswaXdxNGhWeWlwNnNTTnBMNzRDaDVUR08=";
        long expirationTimeSeconds = 3600;

        String accessToken = JwtTokenUtil.generateAccessToken(apiKeySid, expirationTimeSeconds);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success",null,accessToken));
    }
}
