package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.RateRequestDTO;
import com.example.bookswapplatform.service.RateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/rate")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class RateController {
    private final RateService rateService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO> rateUser(Principal principal,
                                                    @RequestParam UUID orderId,
                                                    @Valid @RequestBody RateRequestDTO rateRequestDTO) {
        return rateService.rateUser(principal,orderId, rateRequestDTO);
    }
    @GetMapping("/current-user")
    public ResponseEntity<BaseResponseDTO> getCurrentUserRate(Principal principal) {
        return rateService.viewRate(principal);
    }
    @GetMapping("/other-user")
    public ResponseEntity<BaseResponseDTO> getOtherUserRate(@RequestParam UUID userId) {
        return rateService.viewOtherUserRate(userId);
    }
    @GetMapping("/current-user-create")
    public ResponseEntity<BaseResponseDTO> getCurrentUserRateCreated(Principal principal) {
        return rateService.viewRateByCreateBy(principal);
    }
    @GetMapping("/other-user-create")
    public ResponseEntity<BaseResponseDTO> getOtherUserRateCreated(@RequestParam UUID userId) {
        return rateService.viewOtherUserRateCreate(userId);
    }

}
