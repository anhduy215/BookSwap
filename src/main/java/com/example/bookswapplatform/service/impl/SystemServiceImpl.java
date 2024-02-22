package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.entity.SystemLog.Action;
import com.example.bookswapplatform.entity.SystemLog.Object;
import com.example.bookswapplatform.entity.SystemLog.SystemLog;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.repository.SystemLogRepository;
import com.example.bookswapplatform.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemServiceImpl implements SystemLogService {
    private final ModelMapper modelMapper;
    private final SystemLogRepository systemLogRepository;

    @Override
    public void saveSystemLog(User user, Object object, Action action) {
        SystemLog systemLog = new SystemLog();
        systemLog.setUser(user);
        systemLog.setAction(action);
        systemLog.setObject(object);
        systemLogRepository.save(systemLog);
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllSystemLog() {
        List<SystemLog> systemLogs = systemLogRepository.findAll();
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success",null, systemLogs));
    }
}
