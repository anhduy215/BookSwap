package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Payment.Status;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    @Query("SELECT DISTINCT o from Orders o join o.payments p where o.createBy = :user and p.status = :status")
    Set<Orders> findByPaymentStatusAndCreateBy(User user, Status status);
    @Query("SELECT DISTINCT o from Orders o where o.createBy = :user and o.orderStatus = :orderStatus")
    Set<Orders> findByOrderStatusAndCreateBy(User user, OrderStatus orderStatus);
    @Query("SELECT DISTINCT o from Orders o where o.orderStatus = :orderStatus")
    List<Orders> findByOrderStatus(OrderStatus orderStatus);
    @Query("SELECT DISTINCT o FROM Orders o JOIN o.orderDetails od JOIN od.book b WHERE b.createBy = :user AND o.createBy <> :user " +
            "AND o.orderStatus = :orderStatus ")
    Set<Orders> findOrdersWithBooksCreatedByUser(User user, OrderStatus orderStatus);
    @Query("SELECT DISTINCT o FROM Orders o JOIN o.payments pm JOIN o.post p " +
            "WHERE p.createBy = :user AND o.createBy <> :user AND pm.createBy = :email AND pm.status = :status")
    Set<Orders> findOrdersWithBooksCreatedByUserAndPaymentStatus(User user, String email, Status status);

    @Query("SELECT o from Orders o WHERE o.createBy = :user AND o.isPayment = false ")
    Set<Orders> findOrdersByCreateByAndPayment(User user);

    Set<Orders> findOrdersByCreateBy(User user);

    @Query("SELECT o FROM Orders o WHERE o.autoRejectTime >= :currentTime")
    List<Orders> findOrdersWithAutoRejectTimeBefore(@Param("currentTime") LocalDateTime currentTime);
    @Query("SELECT DISTINCT o FROM Orders o WHERE o.orderStatus = 'WAITING_SHIPPER' and o.locationDetail like %:keyWord% ")
    List<Orders> findByKeyWord(String keyWord);

    @Query("SELECT DISTINCT o FROM Orders o WHERE o.orderStatus = 'WAITING_SHIPPER' and o.district.district = :district")
    List<Orders> findByDistrict(@Param("district") String district);
    @Query("SELECT DISTINCT o FROM Orders o JOIN o.area a JOIN o.district d " +
            "WHERE a.city like %:keyWord% or d.district like %:keyWord% or o.locationDetail like %:keyWord%")
    Page<Orders> searchByKeyWord(String keyWord, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Orders o WHERE o.orderStatus = :orderStatus")
    Page<Orders> searchByStatus(@Param("orderStatus") OrderStatus orderStatus, Pageable pageable);

}
