package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.OrderRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    ResponseEntity<BaseResponseDTO> createOrder(Principal principal, UUID postId, OrderRequest orderRequest);
    ResponseEntity<BaseResponseDTO> getAllRequestOrdersNotPayment(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllRequestOrdersWaitConfirm(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllRequestOrdersWaitShipper(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllRequestOrdersCancel(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllRequestOrdersFinish(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllReceiveOrdersNotPayment(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllReceiveOrdersFinish(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllReceiveOrdersNeedConfirm(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllReceiveOrdersWaitingShipper(Principal principal);
    ResponseEntity<BaseResponseDTO> getAllReceiveOrdersCancel(Principal principal);
    ResponseEntity<BaseResponseDTO> getRequestUserOrdersNotPay(Principal principal);
    ResponseEntity<BaseResponseDTO> getOrderDetail (UUID orderId);
    ResponseEntity<BaseResponseDTO> cancelRequestOrderNotConfirm (Principal principal, UUID orderId);

    //ResponseEntity<BaseResponseDTO> cancel

}
