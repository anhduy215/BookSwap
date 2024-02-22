package com.example.bookswapplatform.vnpay.service.impl;

import com.example.bookswapplatform.entity.Payment.Payment;
import com.example.bookswapplatform.entity.Payment.Status;
import com.example.bookswapplatform.repository.PaymentRepository;
import com.example.bookswapplatform.service.PaymentService;
import com.example.bookswapplatform.vnpay.config.Config;
import com.example.bookswapplatform.vnpay.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
//    @Override
//    public String handleVnPayIPN(Map<String, String> requestParams) {
//        try {
//            String vnpSecureHash = requestParams.get("vnp_SecureHash");
//            requestParams.remove("vnp_SecureHashType");
//            requestParams.remove("vnp_SecureHash");
//            String vnPayCode = requestParams.get("vnp_TxnRef");
//            String amount = requestParams.get("vnp_Amount");
//            Payment payment = paymentRepository.getPaymentsByVnPayCode(vnPayCode);
//
//            if (isValidChecksum(requestParams, vnpSecureHash)) {
//                if (checkOrder(payment)) {
//                    if (checkAmount(amount, payment)) {
//                        if (checkOrderStatus(payment)) {
//                            return handleSuccessResponse(requestParams, payment);
//                        } else {
//                            return "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
//                        }
//                    } else {
//                        return "{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}";
//                    }
//                } else {
//                    return "{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}";
//                }
//            } else {
//                return "{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}";
//            }
//        } catch (UnsupportedEncodingException e) {
//            return "{\"RspCode\":\"99\",\"Message\":\"Unknown error\"}";
//        }
//    }
//    private boolean isValidChecksum(Map<String, String> requestParams, String vnpSecureHash) throws UnsupportedEncodingException {
//        String signValue = Config.hashAllFields(requestParams);
//        return signValue.equals(vnpSecureHash);
//    }
//
    private boolean checkOrderId(Payment payment) {
        // Check if vnp_TxnRef exists in your database
        return payment != null;
    }
//
//    private boolean checkAmount(String amount, Payment payment) {
//        // Check if vnp_Amount is valid
//        BigDecimal paymentAmount = payment.getAmount();
//        BigDecimal vnPayAmount =new BigDecimal(amount);
//        return paymentAmount.compareTo(vnPayAmount.divide(BigDecimal.valueOf(100))) == 0;// replace with your actual logic
//    }
//
//    private boolean checkOrderStatus(Payment payment) {
//        // Check if PaymnentStatus = 0 (pending)
//        return payment.getStatus().equals(Status.ON_GOING);// replace with your actual logic
//    }
//
//    private String handleSuccessResponse(Map<String, String> requestParams, Payment payment) {
//        if ("00".equals(requestParams.get("vnp_ResponseCode"))) {
//            // Here, update PaymnentStatus = 1 in your Database
//            payment.setStatus(Status.SUCCESS);
//            paymentRepository.saveAndFlush(payment);
//            //Add balance
//            paymentService.addBalance(payment);
//            return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
//        } else {
//            // Here, update PaymnentStatus = 2 in your Database
//            payment.setStatus(Status.CANCEL);
//            paymentRepository.saveAndFlush(payment);
//            return "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
//        }
//    }
    @Override
    public String handleVnPayIPN(HttpServletRequest request) {
        try
        {

        /*  IPN URL: Record payment results from VNPAY
        Implementation steps:
        Check checksum
        Find transactions (vnp_TxnRef) in the database (checkOrderId)
        Check the payment status of transactions before updating (checkOrderStatus)
        Check the amount (vnp_Amount) of transactions before updating (checkAmount)
        Update results to Database
        Return recorded results to VNPAY
        */

            // ex:  	PaymnentStatus = 0; pending
            //              PaymnentStatus = 1; success
            //              PaymnentStatus = 2; Faile

            //Begin process return from VNPAY
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            String vnPayCode = request.getParameter("vnp_TxnRef");
            String amount = request.getParameter("vnp_Amount");
            Payment payment = paymentRepository.getPaymentsByVnPayCode(vnPayCode);
            if (fields.containsKey("vnp_SecureHashType"))
            {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash"))
            {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            String signValue = Config.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash))
            {

                //boolean checkOrderId = true; // vnp_TxnRef exists in your database
                boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the
                boolean checkOrderStatus = true; // PaymnentStatus = 0 (pending)


                if(checkOrderId(payment))
                {
                    if(checkAmount)
                    {
                        if (checkOrderStatus)
                        {
                            if ("00".equals(request.getParameter("vnp_ResponseCode")))
                            {
                                //Here Code update PaymnentStatus = 1 into your Database
                                payment.setStatus(Status.SUCCESS);
                                paymentRepository.saveAndFlush(payment);
                                //Add balance
                                paymentService.addBalance(payment);
                            }
                            else
                            {
                                // Here Code update PaymnentStatus = 2 into your Database
                                payment.setStatus(Status.CANCEL);
                            }
                            return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
                        }
                        else
                        {

                            return "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
                        }
                    }
                    else
                    {
                        return "{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}";
                    }
                }
                else
                {
                    return "{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}";
                }
            }
            else
            {
                return "{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}";
            }
        }
        catch(Exception e)
        {
            return "{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}";
        }
    }
}
