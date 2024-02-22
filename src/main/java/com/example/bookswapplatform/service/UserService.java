package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.UpdateUserRequest;
import com.example.bookswapplatform.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.text.ParseException;
import java.util.UUID;

public interface UserService {
    ResponseEntity<BaseResponseDTO> updateUser (Principal principal, UpdateUserRequest updateUserRequest);
    //ResponseEntity<BaseResponseDTO> deleteUser (Principal principal, DeleteUserRequest deleteUserRequest);
    ResponseEntity<BaseResponseDTO> userProfile (Principal principal);
    //ResponseEntity<BaseResponseDTO> ratingUser (Principal principal, UpdateUserRequest updateUserRequest);
    ResponseEntity<BaseResponseDTO> getUserInfo(UUID userId);
    ResponseEntity<BaseResponseDTO> reportUser();
}
