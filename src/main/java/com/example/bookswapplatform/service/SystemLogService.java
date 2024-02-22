package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.entity.SystemLog.Action;
import com.example.bookswapplatform.entity.SystemLog.Object;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface SystemLogService {
    void saveSystemLog(User user, Object object, Action action);
    ResponseEntity<BaseResponseDTO> getAllSystemLog ();
}
