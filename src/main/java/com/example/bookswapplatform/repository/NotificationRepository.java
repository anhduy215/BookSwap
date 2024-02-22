package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("SELECT n from Notification n JOIN n.user u where u.id = :id")
    List<Notification> findAllByUserId(UUID id);
}
