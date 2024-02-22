package com.example.bookswapplatform.vnpay.service;


import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VNPayService {
    String handleVnPayIPN(HttpServletRequest request);
}
