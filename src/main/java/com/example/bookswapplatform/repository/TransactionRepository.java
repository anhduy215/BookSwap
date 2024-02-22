package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Payment.Transaction.Transaction;
import com.example.bookswapplatform.entity.Payment.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllByCreateByOrToWallet(String email, UserWallet userWallet);
}
