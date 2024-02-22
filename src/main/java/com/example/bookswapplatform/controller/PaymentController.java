package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/checkout-request")
    public ResponseEntity<BaseResponseDTO> checkoutForUserRequest (Principal principal, @RequestParam UUID paymentId) {
        return paymentService.checkoutForUserRequest(principal, paymentId);
    }

    @PostMapping("/checkout-get-request")
    public ResponseEntity<BaseResponseDTO> checkoutForUserGetRequest (Principal principal, @RequestParam UUID paymentId) {
        return paymentService.checkoutForUserGetRequest(principal, paymentId);
    }
    @PostMapping("/add-balance")
    public ResponseEntity<BaseResponseDTO> createPaymentForAddBalance(Principal principal, @RequestParam BigDecimal amount) {
        return paymentService.createPaymentForAddBalance(principal, amount);
    }
}
