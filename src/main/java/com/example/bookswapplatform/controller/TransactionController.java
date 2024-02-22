package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transaction")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAllTransaction (Principal principal) {
        return transactionService.getAllTransaction(principal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO> getDetailTransaction (@PathVariable UUID id) {
        return transactionService.getDetailTransaction(id);
    }
}
