package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.UpdateUserRequest;
import com.example.bookswapplatform.dto.UserDTO;
import com.example.bookswapplatform.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_SHIPPER')")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<BaseResponseDTO> getUserProfile(Principal principal) {
        return userService.userProfile(principal);
    }
    @PutMapping("/update-profile")
    public ResponseEntity<BaseResponseDTO> updateProfile(Principal principal, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(principal, updateUserRequest);
    }

    @GetMapping("/user-info")
    public ResponseEntity<BaseResponseDTO> getUserInfo(@RequestParam UUID userId) {
        return userService.getUserInfo(userId);
    }

}
