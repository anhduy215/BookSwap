package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface OrderShippingService {
    ResponseEntity<BaseResponseDTO> createShippingOrder(Principal principal, UUID orderId);
    ResponseEntity<BaseResponseDTO> getOrderShipping (Principal principal, String status);
    ResponseEntity<BaseResponseDTO> takeOrderShipping(Principal principal, UUID orderShippingId);
    ResponseEntity<BaseResponseDTO> getById(UUID orderShippingId);
    ResponseEntity<BaseResponseDTO> changeOrderStatus(Principal principal, UUID orderShippingId, String status);
    ResponseEntity<BaseResponseDTO> filter (String keyWord, String district);
    ResponseEntity<BaseResponseDTO> trackOrderShipping (Principal principal, UUID orderShippingId);

    ResponseEntity<BaseResponseDTO> getOrderShippingForUser(Principal principal, String type);

}
