package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.common.PageableRequest;
import com.example.bookswapplatform.common.Pagination;
import com.example.bookswapplatform.dto.*;
import com.example.bookswapplatform.entity.Order.OrderStatus;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.repository.OrderDetailRepository;
import com.example.bookswapplatform.repository.OrderRepository;
import com.example.bookswapplatform.repository.UserRepository;
import com.example.bookswapplatform.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public ResponseEntity<BaseResponseDTO> orderFilter(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyWord, String status) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize, sortBy, sortOrder);
        Pageable pageable = pageableRequest.toPageable();
        Page<Orders> ordersPage = null;
        List<Orders> ordersList;
        List<OrderDTO> orderDTOS = new ArrayList<>();

        if (keyWord == null && status == null) {
            ordersPage = orderRepository.findAll(pageable);
        } else if (keyWord != null && status == null) {
            ordersPage = orderRepository.searchByKeyWord(keyWord, pageable);
        } else if (keyWord == null && status != null) {
            ordersPage = orderRepository.searchByStatus(OrderStatus.valueOf(status), pageable);
        }

        if (ordersPage != null) {
            ordersList = ordersPage.getContent();
            orderDTOS = convertToOrderDTO(ordersList);
            Pagination pagination = new Pagination(ordersPage.getNumber(), ordersPage.getTotalElements(), ordersPage.getTotalPages());
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", pagination, orderDTOS));
        } else {
            // Handle the case when ordersPage is null (possibly return an error response)
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, null));
        }
    }


    public List<OrderDTO> convertToOrderDTO(List<Orders> orders) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Orders order: orders) {
            orderDTOS.add(orderService.convertToDTO(order));
        }
        return orderDTOS;
    }


    @Override
    public ResponseEntity<BaseResponseDTO> userFilter(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyWord, String role) {
        PageableRequest pageableRequest = new PageableRequest(pageNumber, pageSize, sortBy, sortOrder);
        Pageable pageable = pageableRequest.toPageable();
        Page<User> userPage = null;
        List<User> userList;
        List<UserDTO> userDTOS = new ArrayList<>();
        if (keyWord == null && role == null) {
            userPage = userRepository.findAll(pageable);
        } else if (keyWord != null && role == null) {
            userPage = userRepository.searchByKeyWord(keyWord, pageable);
        } else if (keyWord == null && role != null) {
            userPage = userRepository.searchByRole(role, pageable);
        }
        if (userPage != null) {
            userList = userPage.getContent();
            userDTOS = convertToUserDTO(userList);
            Pagination pagination = new Pagination(userPage.getNumber(), userPage.getTotalElements(), userPage.getTotalPages());
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", pagination, userDTOS));
        } else {
            // Handle the case when ordersPage is null (possibly return an error response)
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, null));
        }
    }
    public List<UserDTO> convertToUserDTO (List<User> userList) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user: userList
             ) {
            userDTOS.add(userService.convertToDTO(user));
        }
        return userDTOS;
    }
}
