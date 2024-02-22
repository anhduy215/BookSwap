package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.StringeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stringee")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class StringeeController {
    private final StringeeService stringeeService;
    @GetMapping("/token")
    public ResponseEntity<BaseResponseDTO> generateAccessToken () {
        return stringeeService.generateAccessToken();
    }
}
