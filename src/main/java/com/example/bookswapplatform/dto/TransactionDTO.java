package com.example.bookswapplatform.dto;

import com.example.bookswapplatform.entity.Payment.Transaction.TransactionType;
import com.example.bookswapplatform.entity.Payment.Transaction.WalletType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private UUID id;
    private LocalDateTime createDate;
    private WalletType walletType;
    private TransactionType transactionType;
    private String status;
    private BigDecimal fee;
    private BigDecimal amount;
    private String description;
    private UserGeneralDTO userGeneralDTO;

}
