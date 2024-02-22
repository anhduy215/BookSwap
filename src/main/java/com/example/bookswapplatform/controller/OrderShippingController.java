package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.OrderShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ship-order")
@RequiredArgsConstructor

public class OrderShippingController {
    private final OrderShippingService orderShippingService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponseDTO> createOrderShipping(Principal principal, @RequestParam UUID orderId) {
        return orderShippingService.createShippingOrder(principal, orderId);
    }

    @PostMapping("/user/id")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponseDTO> trackOrderShipping (Principal principal, @RequestParam UUID orderShippingId) {
        return orderShippingService.trackOrderShipping(principal, orderShippingId);
    }
    @PostMapping("/user/get")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponseDTO> getOrderShippingForUser(Principal principal, @RequestParam String type) {
        return orderShippingService.getOrderShippingForUser(principal, type);
    }
    @PostMapping("/take")
    @PreAuthorize("hasAuthority('ROLE_SHIPPER')")
    public ResponseEntity<BaseResponseDTO> takeOrderShipping(Principal principal, @RequestParam UUID orderShippingId) {
        return orderShippingService.takeOrderShipping(principal, orderShippingId);
    }
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('ROLE_SHIPPER')")
    public ResponseEntity<BaseResponseDTO> getOrderShipping (Principal principal, @RequestParam String status) {
        return orderShippingService.getOrderShipping(principal, status);
    }
    @PostMapping("/status")
    @PreAuthorize("hasAuthority('ROLE_SHIPPER')")
    public ResponseEntity<BaseResponseDTO> changeStatus (Principal principal,
                                                         @RequestParam UUID orderShippingId,
                                                         @RequestParam String status) {
        return orderShippingService.changeOrderStatus(principal, orderShippingId, status);
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_SHIPPER')")
    public ResponseEntity<BaseResponseDTO> getById (@RequestParam UUID id) {
        return orderShippingService.getById(id);
    }
    @PostMapping("/filter")
    @PreAuthorize("hasAuthority('ROLE_SHIPPER')")
    public ResponseEntity<BaseResponseDTO> filter (@RequestParam(required = false) String keyWord,
                                                   @RequestParam(required = false) String district) {
        return orderShippingService.filter(keyWord, district);
    }

}
