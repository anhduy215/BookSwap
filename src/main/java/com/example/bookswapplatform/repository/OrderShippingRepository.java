package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Order.OrderShipping;
import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderShippingRepository extends JpaRepository<OrderShipping, UUID> {
    @Query("SELECT os FROM OrderShipping os WHERE os.orderStatus = :orderStatus")
    List<OrderShipping> findByOrderStatus(@Param("orderStatus") OrderStatus orderStatus);

    @Query("SELECT os FROM OrderShipping os WHERE os.user = :user and os.orderStatus = :orderStatus")
    List<OrderShipping> findByOrderStatusAndCreateBy(@Param("user") User user, @Param("orderStatus") OrderStatus orderStatus);

    @Query("SELECT os FROM OrderShipping os WHERE os.orders = :orders")
    List<OrderShipping> findByOrder(@Param("orders") Orders orders);

    @Query("SELECT os FROM OrderShipping os WHERE os.createBy = :uuid")
    List<OrderShipping> findByCreateBy(UUID uuid);

    @Query("SELECT os FROM OrderShipping os WHERE os.userReceive = :uuid")
    List<OrderShipping> findByUserReceive(UUID uuid);
}
