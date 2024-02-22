package com.example.bookswapplatform.service.impl;


import com.example.bookswapplatform.common.ExchangeMethod;
import com.example.bookswapplatform.dto.*;
import com.example.bookswapplatform.entity.Area.Area;
import com.example.bookswapplatform.entity.Area.District;
import com.example.bookswapplatform.entity.Order.CancelOrderHistory;
import com.example.bookswapplatform.entity.Post.PostStatus;
import com.example.bookswapplatform.entity.SystemLog.Action;
import com.example.bookswapplatform.entity.SystemLog.Object;
import com.example.bookswapplatform.exception.CancelException;
import com.example.bookswapplatform.service.OrderService;
import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.Order.OrderDetail;
import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Payment.Payment;
import com.example.bookswapplatform.entity.Payment.Status;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.*;
import com.example.bookswapplatform.service.PostServiceHelper;
import com.example.bookswapplatform.utils.OrderScheduler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PostRepository postRepository;
    private final AreaRepository areaRepository;
    private final DistrictRepository districtRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PostStatusRepository postStatusRepository;
    private final PostServiceHelper postServiceHelper;
    private final BookServiceImpl bookService;
    private final OrderDetailRepository orderDetailRepository;
    private final UserWalletRepository userWalletRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final CancelOrderHistoryRepository cancelOrderHistoryRepository;
    private final PaymentServiceImpl paymentService;
    private final OrderScheduler orderScheduler;
    private final SystemServiceImpl systemService;
    private final ModelMapper modelMapper;


    //    @Override
//    public ResponseEntity<BaseResponseDTO> createOrder(Principal principal, UUID postId, OrderRequest orderRequest) {
//        Orders orders = new Orders();
//        User user = userRepository.findByFireBaseUid(principal.getName())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new ResourceNotFoundException("Post with id:" + postId + "Not Found!"));
//        for (UUID bookId: orderRequest.getBookIdInPosts()
//             ) {
//            Book book = bookRepository.findById(bookId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Book with id:" + bookId + "Not Found!"));
//            Set<Orders> ordersSet = orderRepository.findOrdersByCreateBy(user);
//            for (Orders ordersCheck : ordersSet
//            ) {
//                 if(orderDetailRepository.findByOrdersAndBook(ordersCheck,book) != null) {
//                     return ResponseEntity.badRequest()
//                             .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Already have order with this book id"));
//                }
//
//            }
//
//        }
//        if (post.getCreateBy() == user) {
//            return ResponseEntity.badRequest()
//                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can't create order with your own post"));
//        }
//        if (!orderRequest.isTradeOrderValid(post.getExchangeMethod())) {
//            return ResponseEntity.badRequest()
//                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Have user book when exchange is sell or not have book trade when exchange is trade"));
//        }
//        //check post available
//        if (post.getPostStatus().getName().equals("LOCKED") || post.getPostStatus().getName().equals("DEACTIVE")) {
//            return ResponseEntity.badRequest()
//                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Post had been locked"));
//        }
//
//        BigDecimal bookPrice = new BigDecimal("0");
//        user.setCity(orderRequest.getCity());
//        user.setDistrict(orderRequest.getDistrict());
//        userRepository.save(user);
//        orders.setCreateBy(user);
//        orders.setPost(post);
//        orders.setNote(orderRequest.getNote());
//        orders.setArea(post.getArea());
//        orders.setUpdateBy(user.getEmail());
//        orders.setOrderStatus(OrderStatus.NOT_PAY);
//        orders.setDistrict(post.getDistrict());
//        orders.setConfirm(false);
//        orders.setPayment(false);
//        orders.setOrderDetails(null);
//        orders.setPayments(null);
//        orders.setAutoRejectTime(LocalDateTime.now().plusHours(1));
//
//        Set<OrderDetail> orderDetails = new HashSet<>();
//
//        for (UUID bookId : orderRequest.getBookTradeIds()
//        ) {
//            Book book = bookRepository.findById(bookId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Book with id:" + bookId + "Not Found!"));
//
//            if (book.isDone() || book.isLock()) {
//                return ResponseEntity.badRequest()
//                        .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Book not available"));
//            }
//            //set book của user tạo đơn khi trade là lock
//            if (user.getBookList().contains(book)) {
//                book.setLock(true);
//            }
//
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setBook(book);
//            orderDetail.setOrders(orders);
//            orderDetail.setPrice(book.getPrice());
//            orderDetails.add(orderDetail);
//
//            bookPrice = bookPrice.add(book.getPrice());
//        }
//        processOrderPrice(orderRequest, orders, bookPrice);
//        orderRepository.save(orders);
//
//        createPaymentForUserRequest(orders);
//
//        orderDetailRepository.saveAll(orderDetails);
//
//        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.CREATED, "Order id: " + orders.getId()));
//    }
    @Override
    public ResponseEntity<BaseResponseDTO> createOrder(Principal principal, UUID postId, OrderRequest orderRequest) {
        Orders orders = new Orders();
        User user = getUser(principal);

        Post post = getPost(postId);

        ResponseEntity<BaseResponseDTO> responseEntity = checkForExistingOrders(user, orderRequest);
        if (responseEntity != null) {
            return responseEntity;
        }

        if (post.getCreateBy() == user) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Can't create order with your own post"));
        }

        responseEntity = validateTradeOrder(post, orderRequest);
        if (responseEntity != null) {
            return responseEntity;
        }

        responseEntity = checkPostAvailability(post);
        if (responseEntity != null) {
            return responseEntity;
        }

        BigDecimal bookPrice = new BigDecimal("0");
        user.setCity(orderRequest.getCity());
        user.setDistrict(orderRequest.getDistrict());
        user.setLocationDetail(orderRequest.getLocationDetail());
        userRepository.save(user);

        initializeOrder(orders, user, post, orderRequest);
        if (checkCancelAvailable(user, orders)) {
            throw new CancelException("You have canceled more than the allowed number of times");
        }

        Set<OrderDetail> orderDetails = createOrderDetails(orderRequest, orders, user, bookPrice);

        processOrderPrice(orderRequest, orders, bookPrice);
        orderRepository.save(orders);

        createPaymentForUserRequest(orders);

        orderDetailRepository.saveAll(orderDetails);

        systemService.saveSystemLog(user, Object.ORDER, Action.CREATE);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.CREATED, "Order id: " + orders.getId()));
    }

    // Extracted Methods

    private User getUser(Principal principal) {
        return userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    private Post getPost(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id:" + postId + "Not Found!"));
    }

    private ResponseEntity<BaseResponseDTO> checkForExistingOrders(User user, OrderRequest orderRequest) {
        for (UUID bookId : orderRequest.getBookIdInPosts()) {
            Book book = getBook(bookId);
            Set<Orders> ordersSet = orderRepository.findOrdersByCreateBy(user);
            for (Orders ordersCheck : ordersSet) {
                if (orderDetailRepository.findByOrdersAndBook(ordersCheck, book) != null) {
                    return ResponseEntity.badRequest()
                            .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Already have order with this book id"));
                }
            }
        }
        return null;
    }

    private ResponseEntity<BaseResponseDTO> validateTradeOrder(Post post, OrderRequest orderRequest) {
        if (!orderRequest.isTradeOrderValid(post.getExchangeMethod())) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Have user book when exchange is sell or not have book trade when exchange is trade"));
        }
        return null;
    }

    private ResponseEntity<BaseResponseDTO> checkPostAvailability(Post post) {
        if (post.getPostStatus().getName().equals("LOCKED") || post.getPostStatus().getName().equals("DEACTIVE")) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Post had been locked"));
        }
        return null;
    }

    private void checkBookAvailability(Book book) {
        if (book.isDone() || book.isLock()) {
            ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Book had been locked"));
        }
    }

    private Set<OrderDetail> createOrderDetails(OrderRequest orderRequest, Orders orders, User user, BigDecimal bookPrice) {
        Set<OrderDetail> orderDetails = new HashSet<>();
        for (UUID bookId : orderRequest.getBookTradeIds()) {
            Book book = getBook(bookId);
            checkBookAvailability(book);
            if (user.getBookList().contains(book)) {
                book.setLock(true);
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(book);
            orderDetail.setOrders(orders);
            orderDetail.setPrice(book.getPrice());
            orderDetails.add(orderDetail);
            bookPrice = bookPrice.add(book.getPrice());
        }
        return orderDetails;
    }


    private Book getBook(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id:" + bookId + "Not Found!"));
    }

    private void initializeOrder(Orders orders, User user, Post post, OrderRequest orderRequest) {
        orders.setCreateBy(user);
        orders.setPost(post);
        orders.setNote(orderRequest.getNote());
        Area area = areaRepository.findByCity(orderRequest.getCity())
                .orElseThrow(() -> new ResourceNotFoundException("City not found!"));
        District district = districtRepository.findByDistrict(orderRequest.getDistrict())
                .orElseThrow(() -> new ResourceNotFoundException("District not found!"));
        orders.setArea(area);
        orders.setDistrict(district);
        orders.setLocationDetail(orderRequest.getLocationDetail());
        orders.setUpdateBy(user.getEmail());
        orders.setOrderStatus(OrderStatus.NOT_PAY);
        orders.setConfirm(false);
        orders.setPayment(false);
        orders.setOrderDetails(null);
        orders.setPayments(null);
        orders.setAutoRejectTime(LocalDateTime.now().plusHours(1));
    }

    public void processOrderPrice(OrderRequest orderRequest, Orders orders, BigDecimal bookPrice) {
        BigDecimal shipPrice = new BigDecimal("15000");
        BigDecimal fee = new BigDecimal("2000");

        orders.setBookPrice(bookPrice);
        orders.setFee(fee);

        if (orderRequest.getIsShip() == 0) {
            shipPrice = BigDecimal.ZERO;
            orders.setSenderShipPrice(shipPrice);
            orders.setReceiverShipPrice(shipPrice);
        } else {
            BigDecimal percentPay = new BigDecimal(orderRequest.getPercentPay())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            shipPrice = shipPrice.multiply(percentPay);
            orders.setSenderShipPrice(shipPrice);
            orders.setShipping(true);
            orders.setReceiverShipPrice(new BigDecimal("15000").subtract(shipPrice));
        }

        BigDecimal senderPrice = calculateSenderPrice(bookPrice, fee, orders.getSenderShipPrice());
        BigDecimal receiverPrice = calculateReceiverPrice(fee, orders.getReceiverShipPrice());

        orders.setSenderPrice(senderPrice);
        orders.setReceiverPrice(receiverPrice);
    }

    private BigDecimal calculateSenderPrice(BigDecimal bookPrice, BigDecimal fee, BigDecimal shipPrice) {
        return bookPrice.add(fee).add(shipPrice);
    }

    private BigDecimal calculateReceiverPrice(BigDecimal fee, BigDecimal shipPrice) {
        return shipPrice.add(fee);
    }

    public void createPaymentForUserRequest(Orders orders) {
        Payment payment = new Payment();
        payment.setAmount(orders.getSenderPrice());
        BigDecimal value = new BigDecimal(2000);
        payment.setFee(value);
        payment.setStatus(Status.ON_GOING);
        payment.setOrders(orders);
        payment.setCreateBy(orders.getCreateBy().getEmail());
        if (orders.getPost().getExchangeMethod().equals(ExchangeMethod.SELL)) {
            payment.setDescription("Chuyển tiền đến " + orders.getPost().getCreateBy().getFirstName());
        } else {
            payment.setDescription("Tiền phí BookSwap");
        }

        paymentRepository.save(payment);

    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersNotPayment(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersList = orderRepository.findByOrderStatusAndCreateBy(user, OrderStatus.NOT_PAY);

        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        for (Orders orders : ordersList
        ) {
            OrderGeneralDTO orderDTO = convertToOrderGeneralDTO(orders, orders.getSenderPrice());
            orderDTOS.add(orderDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersWaitConfirm(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersList = orderRepository.findByOrderStatusAndCreateBy(user, OrderStatus.WAITING_CONFIRM);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        for (Orders orders : ordersList
        ) {
            OrderGeneralDTO orderDTO = convertToOrderGeneralDTO(orders, orders.getSenderPrice());
            orderDTOS.add(orderDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersWaitShipper(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersList = orderRepository.findByOrderStatusAndCreateBy(user, OrderStatus.WAITING_SHIPPER);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        for (Orders orders : ordersList
        ) {
            OrderGeneralDTO orderDTO = convertToOrderGeneralDTO(orders, orders.getSenderPrice());
            orderDTOS.add(orderDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));

    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersCancel(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersList = orderRepository.findByOrderStatusAndCreateBy(user, OrderStatus.CANCEL);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        for (Orders orders : ordersList
        ) {
            OrderGeneralDTO orderDTO = convertToOrderGeneralDTO(orders, orders.getSenderPrice());
            orderDTOS.add(orderDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllRequestOrdersFinish(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersList = orderRepository.findByOrderStatusAndCreateBy(user, OrderStatus.FINISH);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        for (Orders orders : ordersList
        ) {
            OrderGeneralDTO orderDTO = convertToOrderGeneralDTO(orders, orders.getSenderPrice());
            orderDTOS.add(orderDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersNeedConfirm(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersSet = orderRepository.findOrdersWithBooksCreatedByUser(user, OrderStatus.WAITING_CONFIRM);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        ordersSet.forEach(orders -> {
            OrderGeneralDTO orderGeneralDTO = convertToOrderGeneralDTO(orders, orders.getReceiverPrice());
            orderDTOS.add(orderGeneralDTO);
        });

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersNotPayment(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersSet = orderRepository.findOrdersWithBooksCreatedByUserAndPaymentStatus(user, user.getEmail(), Status.ON_GOING);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        ordersSet.forEach(orders -> {
            OrderGeneralDTO orderGeneralDTO = convertToOrderGeneralDTO(orders, orders.getReceiverPrice());
            orderDTOS.add(orderGeneralDTO);
        });

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersFinish(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersSet = orderRepository.findOrdersWithBooksCreatedByUser(user, OrderStatus.FINISH);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        ordersSet.forEach(orders -> {
            OrderGeneralDTO orderGeneralDTO = convertToOrderGeneralDTO(orders, orders.getReceiverPrice());
            orderDTOS.add(orderGeneralDTO);
        });

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersWaitingShipper(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersSet = orderRepository.findOrdersWithBooksCreatedByUser(user, OrderStatus.WAITING_SHIPPER);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        ordersSet.forEach(orders -> {
            OrderGeneralDTO orderGeneralDTO = convertToOrderGeneralDTO(orders, orders.getReceiverPrice());
            orderDTOS.add(orderGeneralDTO);
        });

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllReceiveOrdersCancel(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<Orders> ordersSet = orderRepository.findOrdersWithBooksCreatedByUser(user, OrderStatus.CANCEL);
        Set<OrderGeneralDTO> orderDTOS = new HashSet<>();
        ordersSet.forEach(orders -> {
            OrderGeneralDTO orderGeneralDTO = convertToOrderGeneralDTO(orders, orders.getReceiverPrice());
            orderDTOS.add(orderGeneralDTO);
        });

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getRequestUserOrdersNotPay(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Set<OrderGeneralDTO> orderDTOSet = new HashSet<>();
        Set<Orders> ordersList = orderRepository.findOrdersByCreateByAndPayment(user);

        for (Orders orders : ordersList
        ) {
            OrderGeneralDTO orderDTO = convertToOrderGeneralDTO(orders, orders.getSenderPrice());
            orderDTOSet.add(orderDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, orderDTOSet));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getOrderDetail(UUID orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, convertToDTO(orders)));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> cancelRequestOrderNotConfirm(Principal principal, UUID orderId) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        cancelRequestOrderNotConfirmFunction(user, orders);
        systemService.saveSystemLog(user, Object.ORDER, Action.CANCEL);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Cancel success"));
    }

    public boolean checkCancelAvailable(User user, Orders orders) {
        long cancelCountForOrder = user.getCancellationHistories()
                .stream()
                .filter(history -> history.getOrders().getPost().equals(orders.getPost()))
                .count();
        // Kiểm tra xem lịch sử hủy đơn có tồn tại và cancelCount >= 3 không
        return cancelCountForOrder >= 3;
    }

    // Lưu thông tin lịch sử hủy đơn
    public void saveCancelHistory(User user, Orders orders) {
        CancelOrderHistory cancelOrderHistory = new CancelOrderHistory();
        cancelOrderHistory.setUser(user);
        cancelOrderHistory.setOrders(orders);
        cancelOrderHistoryRepository.save(cancelOrderHistory);
    }

    public void cancelRequestOrderNotConfirmFunction(User user, Orders orders) {
        if (orders.getOrderStatus().equals(OrderStatus.WAITING_CONFIRM)) {
            orders.setOrderStatus(OrderStatus.CANCEL);
            orders.setUserCancel(user.getEmail());
            paymentService.createCancelOrderPayment(orders);
            //đổi trang thái book không lock nữa
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrders(orders);
            for (OrderDetail orderDetail : orderDetails
            ) {
                orderDetail.getBook().setLock(false);
            }
            //gở sách khỏi order
            orderDetailRepository.deleteAll(orderDetails);
            //đổi trạng thái post nếu post đang bị lock
            PostStatus locked = postStatusRepository.findByName("LOCKED")
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
            PostStatus active = postStatusRepository.findByName("ACTIVE")
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found!"));
            if (orders.getPost().getPostStatus() == locked) {
                orders.getPost().setPostStatus(active);
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
            orders.setOrderStatus(OrderStatus.CANCEL);
            orders.setUserCancel(user.getEmail());
            //đổi trang thái book không lock nữa
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrders(orders);
            for (Book bookInOrder : orderDetailRepository.findBooksInOrder(orders)) {
                orders.getPost().getBooks().forEach(bookInPost -> {
                    if (!bookInOrder.equals(bookInPost)) {
                        bookInOrder.setLock(false);
                        bookRepository.saveAndFlush(bookInOrder);
                    }
                });
            }
            //gở sách khỏi order
            orderDetailRepository.deleteAll(orderDetails);
            orderRepository.save(orders);
            // xóa đơn thanh toán
            for (Payment payment : orders.getPayments()
            ) {
                if (payment.getStatus().equals(Status.ON_GOING)) {
                    paymentRepository.deleteById(payment.getId());
                }

            }

        }

        //nếu user không phải là người tạo đơn
        if (user != orders.getPost().getCreateBy()) {
            //luu save cancel history
            saveCancelHistory(user, orders);
        }

    }

    public OrderDTO convertToDTO(Orders orders) {
        if (orders == null) {
            return null;
        }
        Set<BookGeneralDTO> bookDTOS = new HashSet<>();
        OrderDTO orderDTO = modelMapper.map(orders, OrderDTO.class);
        orderDTO.setUserOrderDTO(convertToUserOrderDTO(orders));
        orderDTO.setCancelBy(orders.getUserCancel());
        orderDTO.setCity(orders.getArea().getCity());
        orderDTO.setBookPrice(orders.getBookPrice());
        orderDTO.setDistrict(orders.getDistrict().getDistrict());
        PostDTO postDTO = postServiceHelper.convertToDTO(orders.getPost());
        orderDTO.setPostDTO(postDTO);
        for (Book book : orderDetailRepository.findBooksInOrder(orders)
        ) {
            BookGeneralDTO bookDTO = bookService.convertToGeneralDTO(book);
            bookDTOS.add(bookDTO);
        }
        orderDTO.setBookTradeDTOS(bookDTOS);

        Set<PaymentDTO> paymentDTOS = new HashSet<>();
        for (Payment payment : orders.getPayments()) {
            PaymentDTO paymentDTO = paymentService.convertToDTO(payment);
            paymentDTOS.add(paymentDTO);
        }
        orderDTO.setPaymentDTOS(paymentDTOS);

        return orderDTO;

    }

    public UserOrderDTO convertToUserOrderDTO(Orders orders) {
        if (orders == null) {
            return null;
        }
        User user = orders.getCreateBy();
        UserOrderDTO userOrderDTO = new UserOrderDTO();
        String name = user.getLastName() + " " + user.getFirstName();
        userOrderDTO.setEmail(user.getEmail());
        userOrderDTO.setId(user.getId());
        userOrderDTO.setPhone(user.getPhone());
        userOrderDTO.setName(name);
        userOrderDTO.setCity(user.getCity());
        userOrderDTO.setDistrict(user.getDistrict());
        userOrderDTO.setFireBaseId(user.getFireBaseUid());
        userOrderDTO.setImgUrl(user.getImage());
        return userOrderDTO;
    }

    public OrderGeneralDTO convertToOrderGeneralDTO(Orders orders, BigDecimal price) {
        if (orders == null) {
            return null;
        }
        OrderGeneralDTO orderGeneralDTO = new OrderGeneralDTO();
        orderGeneralDTO.setId(orders.getId());
        orderGeneralDTO.setPrice(price);
        orderGeneralDTO.setCreateDate(orders.getCreateDate());
        orderGeneralDTO.setOrderStatus(orders.getOrderStatus());
        orderGeneralDTO.setConfirm(orders.isConfirm());
        orderGeneralDTO.setPayment(orders.isPayment());
        orderGeneralDTO.setCancelBy(orders.getUserCancel());
        orderGeneralDTO.setUserOrderDTO(convertToUserOrderDTO(orders));
        PostGeneralDTO postGeneralDTO = postServiceHelper.convertToGeneralDTO(orders.getPost());
        orderGeneralDTO.setPostGeneralDTO(postGeneralDTO);
        Set<BookGeneralDTO> bookDTOS = new HashSet<>();
        for (Book book : orderDetailRepository.findBooksInOrder(orders)
        ) {
            BookGeneralDTO bookDTO = bookService.convertToGeneralDTO(book);
            bookDTOS.add(bookDTO);
        }
        orderGeneralDTO.setBookGeneralDTOS(bookDTOS);
        return orderGeneralDTO;
    }
}

