package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.common.ExchangeMethod;
import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.PaymentDTO;
import com.example.bookswapplatform.entity.Payment.Transaction.TransactionType;
import com.example.bookswapplatform.entity.Payment.Transaction.WalletType;
import com.example.bookswapplatform.entity.Post.PostStatus;
import com.example.bookswapplatform.entity.SystemLog.Action;
import com.example.bookswapplatform.entity.SystemLog.Object;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.service.PaymentService;
import com.example.bookswapplatform.entity.Book.Book;
import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.Payment.Payment;
import com.example.bookswapplatform.entity.Payment.Status;
import com.example.bookswapplatform.entity.Payment.Transaction.Transaction;
import com.example.bookswapplatform.entity.Payment.UserWallet;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.*;
import com.example.bookswapplatform.vnpay.config.Config;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PostStatusRepository postStatusRepository;
    private final BookRepository bookRepository;
    private final PostRepository postRepository;
    private final TransactionRepository transactionRepository;
    private final UserWalletRepository userWalletRepository;
    private final UserRepository userRepository;
    private final SystemServiceImpl systemService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<BaseResponseDTO> checkoutForUserRequest(Principal principal, UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found!"));

        if(!payment.getStatus().equals(Status.ON_GOING)) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Must be payment on-going"));
        }
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Orders orders = payment.getOrders();
        Post post = orders.getPost();
        boolean bookIsLock = orderDetailRepository.findReceiveBooksInOrder(orders, post.getCreateBy()).stream().allMatch(Book::isLock);
        if(bookIsLock) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Book is lock"));
        }

        UserWallet userWallet = user.getUserWallet();
        if(userWallet.getBalance().compareTo(payment.getAmount()) < 0) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Not have enough balance"));

        }
        BigDecimal newBalance = userWallet.getBalance().subtract(payment.getAmount());
        userWallet.setBalance(newBalance);
        payment.setStatus(Status.SUCCESS);

        if(post.getExchangeMethod().equals(ExchangeMethod.SELL)) {
            createTransaction(payment,post.getCreateBy());
            createReceiveTransaction(payment,post.getCreateBy());
        }else {
            createTransaction(payment, null);
        }


        orders.setOrderStatus(OrderStatus.WAITING_CONFIRM);
        //đổi trạng thái của book thành lock

        //post.getBooks().forEach(book -> book.setLock(true));
        List<Book> books = orderDetailRepository.findBooksInOrder(orders);
        for (Book book: books
        ) {
            book.setLock(true);
            bookRepository.save(book);

        }
        orderRepository.save(orders);
        //khi thanh toán đơn xong nếu trong post có tất cả sách đã lock thì đổi status của post thành locked
        PostStatus postStatus = postStatusRepository.findByName("LOCKED")
                .orElseThrow(() -> new ResourceNotFoundException("Post status not found!"));
        post.checkAndSetLockedStatus(postStatus);
        postRepository.save(orders.getPost());
        //tạo thanh toán cho bên bán để xác nhận đơn
        createPaymentForUserGetRequest(orders);

        systemService.saveSystemLog(user, Object.PAYMENT, Action.CREATE);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully"));
    }
    public void createPaymentForUserGetRequest(Orders orders) {
        if(!orders.isConfirm()) {
            Payment payment = new Payment();
            payment.setAmount(orders.getReceiverPrice());
            payment.setFee(BigDecimal.valueOf(2000));
            payment.setStatus(Status.ON_GOING);
            payment.setOrders(orders);
            payment.setCreateBy(orders.getPost().getCreateBy().getEmail());
            payment.setDescription("Tiền phí BookSwap");

            paymentRepository.save(payment);

        }

    }
    public void createCancelOrderPayment(Orders orders) {
        if(orders.getOrderStatus().equals(OrderStatus.CANCEL)) {
            Payment payment = new Payment();
            payment.setAmount(orders.getSenderPrice());
            payment.setFee(BigDecimal.valueOf(2000));
            payment.setStatus(Status.SUCCESS);
            payment.setOrders(orders);
            payment.setCreateBy("Book Swap");

            User user = orders.getCreateBy();
            UserWallet userWallet = user.getUserWallet();
            BigDecimal balance = userWallet.getBalance();
            BigDecimal senderPrice = orders.getSenderPrice();
            userWallet.setBalance(balance.add(senderPrice));
            paymentRepository.save(payment);
            createTransaction(payment, user);
            createReceiveTransaction(payment, user);
        }
    }

    public void createReceiveTransaction(Payment payment, User user) {
        Transaction transaction = new Transaction();
        //transaction.setToWallet(user.getUserWallet());
        transaction.setAmount(payment.getAmount());
        transaction.setFee(payment.getFee());
        transaction.setStatus(String.valueOf(payment.getStatus()));
        transaction.setWalletType(WalletType.BOOKSWAP);
        transaction.setTransactionType(TransactionType.RECEIVE);
        transaction.setCreateBy(user.getEmail());
        transaction.setPayment(payment);
        transaction.setDescription("Nhận tiền từ " + payment.getCreateBy());
        transactionRepository.save(transaction);
    }
    public void createTransaction(Payment payment, User user) {
        Transaction transaction = new Transaction();
        if (user == null) {
            transaction.setToWallet(null);
        } else {
            transaction.setToWallet(user.getUserWallet());
        }
        transaction.setAmount(payment.getAmount());
        transaction.setFee(payment.getFee());
        transaction.setCreateBy(payment.getCreateBy());
        transaction.setStatus(String.valueOf(payment.getStatus()));
        transaction.setDescription(payment.getDescription());
        transaction.setWalletType(WalletType.BOOKSWAP);
        if(user == null) {
            transaction.setTransactionType(TransactionType.FEE);
        }else {
            transaction.setTransactionType(TransactionType.SEND);
        }

        transaction.setPayment(payment);
        transactionRepository.save(transaction);

    }

    @Override
    public ResponseEntity<BaseResponseDTO> checkoutForUserGetRequest(Principal principal,UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found!"));
        if(!payment.getStatus().equals(Status.ON_GOING)) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Must be payment on-going"));
        }
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Orders orders = payment.getOrders();

        UserWallet userWallet = user.getUserWallet();
        if(userWallet.getBalance().compareTo(payment.getAmount()) < 0) {
            return ResponseEntity.badRequest()
                    .body(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Not have enough balance"));

        }
        BigDecimal newBalance = userWallet.getBalance().subtract(payment.getAmount()).add(orders.getBookPrice());
        userWallet.setBalance(newBalance);
        payment.setStatus(Status.SUCCESS);

        orders.setConfirm(true);
        orders.setPayment(true);
        //xu ly ben post va book
        List<Book> books = orderDetailRepository.findBooksInOrder(orders);
        for (Book book: books
        ) {
            book.setDone(true);
            bookRepository.save(book);

        }
        //nếu đơn không có giá ship thì sẽ đổi trạng thái thành finish
        BigDecimal zero = BigDecimal.ZERO;
        if(orders.getSenderShipPrice().compareTo(zero) == 0 && orders.getReceiverShipPrice().compareTo(zero) == 0) {
            orders.setOrderStatus(OrderStatus.FINISH);
        } else {
            orders.setOrderStatus(OrderStatus.WAITING_SHIPPER);


        }
        orderRepository.save(orders);

        Post post = orders.getPost();
        //tạo transaction khi thanh toán thành công
        createTransaction(payment,null);
        //check neu tat ca book trong post da hoan thanh thi doi trang thai post
        Set<Book> bookSet = post.getBooks();

        if(bookSet.stream().allMatch(Book::isDone)) {
            post.setPostStatus(postStatusRepository.findByName("DEACTIVE")
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found!")));
            postRepository.save(post);
        }
        systemService.saveSystemLog(user, Object.PAYMENT, Action.CREATE);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully"));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> createPaymentForAddBalance(Principal principal, BigDecimal amount) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        Payment payment = new Payment();
        String vnPayCode = Config.getRandomNumber(8);
        payment.setStatus(Status.ON_GOING);
        payment.setAmount(amount);
        payment.setCreateBy(user.getEmail());
        payment.setFee(BigDecimal.valueOf(0));
        payment.setOrders(null);
        payment.setDescription("Nạp tiền vào ví");
        payment.setVnPayCode(vnPayCode);
        paymentRepository.save(payment);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, vnPayCode));
    }

    @Override
    public void addBalance(Payment payment) {
        User user = userRepository.findByEmail(payment.getCreateBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        Transaction transaction = new Transaction();
        transaction.setToWallet(null);
        transaction.setAmount(payment.getAmount());
        transaction.setFee(payment.getFee());
        transaction.setCreateBy(payment.getCreateBy());
        transaction.setStatus(String.valueOf(payment.getStatus()));
        transaction.setDescription(payment.getDescription());
        transaction.setWalletType(WalletType.VNPAY);
        transaction.setTransactionType(TransactionType.ADD);
        transaction.setPayment(payment);

        transactionRepository.save(transaction);
        UserWallet userWallet = user.getUserWallet();
        BigDecimal balance = userWallet.getBalance();
        userWallet.setBalance(balance.add(payment.getAmount()));
        userWalletRepository.save(userWallet);

    }

    public PaymentDTO convertToDTO (Payment payment) {
        if(payment == null ) {
            return null;
        }
        return modelMapper.map(payment, PaymentDTO.class);
    }

}
