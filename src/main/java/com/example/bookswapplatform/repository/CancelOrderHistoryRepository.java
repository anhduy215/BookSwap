package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Order.CancelOrderHistory;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CancelOrderHistoryRepository extends JpaRepository<CancelOrderHistory, UUID> {
    CancelOrderHistory findByUserAndOrders (User user, Orders orders);
    List<CancelOrderHistory> findByUser (User user);
}
