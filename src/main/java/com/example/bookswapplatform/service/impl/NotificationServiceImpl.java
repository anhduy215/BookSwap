package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.NotificationDTO;
import com.example.bookswapplatform.dto.NotificationRequest;
import com.example.bookswapplatform.entity.Notification;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.NotificationRepository;
import com.example.bookswapplatform.repository.UserRepository;
import com.example.bookswapplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<BaseResponseDTO> save(Principal principal, NotificationRequest notificationRequest, UUID userId) {
        User currentUser = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));

        Notification notification = new Notification();
        if(userId == null) {
            notification.setUser(currentUser);
        } else {
            User otherUser = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
            notification.setUser(otherUser);
        }
        notification.setName(notificationRequest.getName());
        notification.setDescription(notificationRequest.getDescription());
        notification.setType(notificationRequest.getType());
        if(notificationRequest.getOrderId() == null || notificationRequest.getOrderId().isEmpty()) {
            notification.setOrderId(null);
        } else {
            notification.setOrderId(notificationRequest.getOrderId());
        }
        notificationRepository.save(notification);

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.CREATED, "Success"));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAll(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));
        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        if(notifications.isEmpty()) {
            notificationDTOS = null;
        } else {
            for (Notification notification: notifications
                 ) {
                notificationDTOS.add(convertToDTO(notification));
            }
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, notificationDTOS));
    }

    public NotificationDTO convertToDTO (Notification notification) {
        if (notification == null ) {
            return null;
        }
        return modelMapper.map(notification, NotificationDTO.class);

    }
}
