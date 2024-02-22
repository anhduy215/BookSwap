package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.SystemLog.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemLogRepository extends JpaRepository<SystemLog, UUID> {
}
