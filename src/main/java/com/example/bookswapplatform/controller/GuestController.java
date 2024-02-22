package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.security.firebase.service.UserManagementService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/guest")
@RequiredArgsConstructor
public class GuestController {
    private final UserManagementService userManagementService;

    @PostMapping("/user-claims")
    public ResponseEntity<BaseResponseDTO> setUserClaims(Principal principal) throws FirebaseAuthException {
        String uid = principal.getName();
        return userManagementService.setUserClaims(uid);
    }
    @PostMapping("/change-claims")
    public ResponseEntity<BaseResponseDTO> changerUserClaims(Principal principal) throws FirebaseAuthException {
        return userManagementService.changeUserClaims(principal);
    }
}
