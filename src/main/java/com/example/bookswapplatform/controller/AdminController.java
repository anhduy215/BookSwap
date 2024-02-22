package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.AdminService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class AdminController {
    private final AdminService adminService;
    @PostMapping("/order/filter")
    public ResponseEntity<BaseResponseDTO> orderFilter(@Min(value = 0, message = "pageNumber must be greater than or equal to 0")
                                                @RequestParam(defaultValue = "0") int page,

                                                @Min(value = 1, message = "pageSize must be greater than or equal to 1")
                                                @Max(value = 100, message = "pageSize must be less than or equal to 100")
                                                @RequestParam(defaultValue = "6") int size,

                                                @Parameter(description = "Sort by (EX: receiverPrice, senderPrice, bookPrice...)")
                                                @RequestParam(defaultValue = "senderPrice") String sortBy,

                                                @Parameter(description = "Sort order (EX: asc, desc)")
                                                @RequestParam(defaultValue = "desc") String sortOrder,

                                                @RequestParam(required = false) String keyWord,

                                                @Parameter(description = "Status (EX: NOT_PAY, WAITING_CONFIRM, WAITING_SHIPPER,PREPARING, ON_GOING, FINISH, CANCEL)")
                                                @RequestParam(required = false) String status) {
        return adminService.orderFilter(page, size, sortBy, sortOrder, keyWord, status);

    }

    @PostMapping("/user/filter")
    public ResponseEntity<BaseResponseDTO> userFilter(@Min(value = 0, message = "pageNumber must be greater than or equal to 0")
                                                       @RequestParam(defaultValue = "0") int page,

                                                       @Min(value = 1, message = "pageSize must be greater than or equal to 1")
                                                       @Max(value = 100, message = "pageSize must be less than or equal to 100")
                                                       @RequestParam(defaultValue = "6") int size,

                                                       @Parameter(description = "Sort by (EX: lastName,email,...)")
                                                       @RequestParam(defaultValue = "firstName") String sortBy,

                                                       @Parameter(description = "Sort order (EX: asc, desc)")
                                                       @RequestParam(defaultValue = "desc") String sortOrder,

                                                       @RequestParam(required = false) String keyWord,

                                                       @Parameter(description = "Role (EX: USER, ADMIN, SHIPPER )")
                                                       @RequestParam(required = false) String role) {
        return adminService.userFilter(page, size, sortBy, sortOrder, keyWord, role);

    }

}
