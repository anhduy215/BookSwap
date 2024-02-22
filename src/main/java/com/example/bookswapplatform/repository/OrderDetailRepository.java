package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.Order.OrderDetail;
import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    @Query("SELECT od FROM OrderDetail od WHERE od.orders.isConfirm = true AND od.book = :book")
    List<OrderDetail> findConfirmedOrderDetailsForBooks(@Param("book") Book book);

    @Query("SELECT od.book FROM OrderDetail od WHERE od.orders = :orders")
    List<Book> findBooksInOrder(@Param("orders") Orders orders);
    List<OrderDetail> findByOrders(Orders orders);
    OrderDetail findByOrdersAndBook(Orders orders, Book book);
    @Query("SELECT od.book FROM OrderDetail od WHERE od.orders = :orders AND od.book.createBy = :user")
    List<Book> findReceiveBooksInOrder(Orders orders, User user);


}
