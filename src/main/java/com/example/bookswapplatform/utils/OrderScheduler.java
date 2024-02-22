package com.example.bookswapplatform.utils;

import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.Order.OrderDetail;
import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Payment.Payment;
import com.example.bookswapplatform.entity.Payment.Status;
import com.example.bookswapplatform.entity.Payment.UserWallet;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.Post.PostStatus;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private static final long PERIOD = 360000; // 1 giờ (trong mili giây)
    private final OrderRepository orderRepository;
    private final PostStatusRepository postStatusRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookRepository bookRepository;
    private final PostRepository postRepository;

    //    public void startOrderAutoRejectScheduler(Orders orders) {
//        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), orders.getAutoRejectTime());
//        Timer timer = new Timer(true);
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                checkAndAutoRejectOrder(orders);
//            }
//        }, delay, PERIOD);
//    }
    @Scheduled(fixedRate = PERIOD)
    public void autoRejectOrders() {
        List<Orders> ordersToAutoReject = orderRepository.findOrdersWithAutoRejectTimeBefore(LocalDateTime.now());

        for (Orders orders : ordersToAutoReject) {
            long delayMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), orders.getAutoRejectTime());
            long delay = Math.max(delayMillis, 0);
            try {
                Thread.sleep(delay); // Add delay before processing each order
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // Handle the interruption if needed
            }
            cancelOrder(orders);

        }
    }
    @Transactional
    private void cancelOrder(Orders orders) {
        // Thực hiện hành động từ chối đơn tự động ở đây
        if (orders.getOrderStatus().equals(OrderStatus.WAITING_CONFIRM)) {
            orders.setUserCancel("Book Swap");
            orders.setOrderStatus(OrderStatus.CANCEL);

            UserWallet userWallet = orders.getCreateBy().getUserWallet();
            BigDecimal balance = orders.getCreateBy().getUserWallet().getBalance();
            BigDecimal senderPrice = orders.getSenderPrice();
            userWallet.setBalance(balance.add(senderPrice));
            //đổi trang thái book không lock nữa
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrders(orders);
            for (OrderDetail orderDetail : orderDetails
            ) {
                Book book = orderDetail.getBook();
                if (book.isLock()) {
                    book.setLock(false);
                    bookRepository.saveAndFlush(book);
                }
            }
            //gở sách khỏi order
            orderDetailRepository.deleteAll(orderDetails);
            //đổi trạng thái post nếu post đang bị lock
            PostStatus locked = postStatusRepository.findByName("LOCKED")
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
            PostStatus active = postStatusRepository.findByName("ACTIVE")
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
            Post post = postRepository.getPostByOrder(orders);
            if (post.getPostStatus() == locked) {
                post.setPostStatus(active);
                postRepository.save(post);
            }
            orderRepository.save(orders);

            for (Payment payment : orders.getPayments()
            ) {
                if (payment.getStatus().equals(Status.ON_GOING)) {
                    paymentRepository.deleteById(payment.getId());
                }

            }

        }
        if (orders.getOrderStatus().equals(OrderStatus.NOT_PAY)) {
            orders.setUserCancel("Book Swap");
            orders.setOrderStatus(OrderStatus.CANCEL);


            List<OrderDetail> orderDetails = orderDetailRepository.findByOrders(orders);
            for (Book bookInOrder : orderDetailRepository.findBooksInOrder(orders)) {
                orders.getPost().getBooks().forEach(bookInPost -> {
                    if (!bookInOrder.equals(bookInPost)) {
                        bookInOrder.setLock(false);
                        bookRepository.saveAndFlush(bookInOrder);
                    }
                });
            }
            orderDetailRepository.deleteAll(orderDetails);
            orderRepository.save(orders);
            for (Payment payment : orders.getPayments()
            ) {
                if (payment.getStatus().equals(Status.ON_GOING)) {
                    paymentRepository.deleteById(payment.getId());
                }

            }
        }

    }

}
