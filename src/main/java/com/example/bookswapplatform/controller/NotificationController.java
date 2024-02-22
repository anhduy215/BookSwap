package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.NotificationRequest;
import com.example.bookswapplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/notification")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponseDTO> save (Principal principal,
                                                 @RequestBody NotificationRequest notificationRequest,
                                                 @RequestParam(required = false) UUID userId) {
        return notificationService.save(principal, notificationRequest, userId);
    }

    @GetMapping
    public ResponseEntity<BaseResponseDTO> getAll (Principal principal) {
        return notificationService.getAll(principal);
    }
}
