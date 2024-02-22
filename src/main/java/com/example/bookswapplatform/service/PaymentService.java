package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.entity.Payment.Payment;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

public interface PaymentService {
//    ResponseEntity<BaseResponseDTO> getPayment(UUID orderId);

    ResponseEntity<BaseResponseDTO> checkoutForUserRequest(Principal principal, UUID paymentId);
    ResponseEntity<BaseResponseDTO> checkoutForUserGetRequest(Principal principal, UUID paymentId);
    ResponseEntity<BaseResponseDTO> createPaymentForAddBalance(Principal principal, BigDecimal amount);
    void addBalance (Payment payment);
}
