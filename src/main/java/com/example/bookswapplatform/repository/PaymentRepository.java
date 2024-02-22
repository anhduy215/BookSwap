package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Payment.Payment;
import com.example.bookswapplatform.entity.Payment.Status;
import com.example.bookswapplatform.entity.User.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Query("SELECT p from Payment p JOIN p.orders o WHERE p.orders = :orders AND p.createBy = :user AND o.isPayment = false ")
    Optional<Payment> getPaymentsByOrders (Orders orders, User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.id = :id")
    void deleteById(UUID id);
    @Query("SELECT p FROM Payment p JOIN p.orders o WHERE o.id = :orderId")
    List<Payment> getPaymentsByOrdersId(UUID orderId);

    List<Payment> getPaymentsByStatus(Status status);
    Payment getPaymentsByVnPayCode(String vnPayCode);
}
