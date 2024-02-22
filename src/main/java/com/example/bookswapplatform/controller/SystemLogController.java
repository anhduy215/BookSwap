package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/log")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class SystemLogController {
    private final SystemLogService systemLogService;
    @GetMapping()
    public ResponseEntity<BaseResponseDTO> getSystemLog() {
        return systemLogService.getAllSystemLog();
    }
}
