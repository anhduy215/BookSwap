package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.OrderRequest;
import com.example.bookswapplatform.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO> createOrder (Principal principal,
                                                        @RequestParam UUID postId,
                                                        @Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(principal,postId, orderRequest);
    }

    @Operation(description = "Đây là api lấy đơn mua chưa thanh toán")
    @GetMapping("/request-not-payment")
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersNotPayment(Principal principal) {
        return orderService.getAllRequestOrdersNotPayment(principal);
    }
    @Operation(description = "Đây là api lấy đơn mua đã xong")
    @GetMapping("/request-finish")
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersFinish(Principal principal) {
        return orderService.getAllRequestOrdersFinish(principal);
    }
    @Operation(description = "Đây là api lấy đơn mua đang chờ shipper")
    @GetMapping("/request-wait-shipper")
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersWaitShipper(Principal principal) {
        return orderService.getAllRequestOrdersWaitShipper(principal);
    }
    @Operation(description = "Đây là api lấy đơn mua chờ xác nhận")
    @GetMapping("/request-wait-confirm")
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersWaitConfirm(Principal principal) {
        return orderService.getAllRequestOrdersWaitConfirm(principal);
    }
    @Operation(description = "Đây là api lấy đơn mua đã hủy")
    @GetMapping("/request-cancel")
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersCancel(Principal principal) {
        return orderService.getAllRequestOrdersCancel(principal);
    }

    @Operation(description = "Đây là api lấy đơn bán chưa thanh toán")
    @GetMapping("/receive-not-payment")
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersNotPayment(Principal principal) {
        return orderService.getAllReceiveOrdersNotPayment(principal);
    }
    @Operation(description = "Đây là api lấy đơn bán đã xon")
    @GetMapping("/receive-finish")
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersFinish(Principal principal) {
        return orderService.getAllReceiveOrdersFinish(principal);
    }
    @Operation(description = "Đây là api lấy đơn bán cần xác nhận")
    @GetMapping("/receive-need-confirm")
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersNeedConfirm(Principal principal) {
        return orderService.getAllReceiveOrdersNeedConfirm(principal);
    }
    @Operation(description = "Đây là api lấy đơn bán chờ shipper")
    @GetMapping("/receive-wait-shipper")
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersWaitingShipper(Principal principal) {
        return orderService.getAllReceiveOrdersWaitingShipper(principal);
    }
    @Operation(description = "Đây là api lấy đơn bán đã hủy")
    @GetMapping("/receive-cancel")
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersCancel(Principal principal) {
        return orderService.getAllReceiveOrdersCancel(principal);
    }
    @GetMapping("/id")
    public ResponseEntity<BaseResponseDTO> getOrderDetail(@RequestParam  UUID orderId) {
        return orderService.getOrderDetail(orderId);
    }
    @Operation(description = "Đây là api người mua từ chối xác nhận đơn và người bán hủy đơn trong lúc wait confirm")
    @PostMapping("/cancel")
    public ResponseEntity<BaseResponseDTO> cancelRequestConfirmOrder(Principal principal, @RequestParam UUID orderId) {
        return orderService.cancelRequestOrderNotConfirm(principal, orderId);
    }

}
