package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/area")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class AreaController {
    private final AreaService areaService;
    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAll() {
        return areaService.getAllArea();
    }

    @GetMapping("/city")
    public ResponseEntity<BaseResponseDTO> getCity() {
        return areaService.getCity();
    }

    @GetMapping("/district")
    public ResponseEntity<BaseResponseDTO> getDistrict(@RequestParam String city) {
        return areaService.getDistrict(city);
    }
    @GetMapping("/all/district")
    public ResponseEntity<BaseResponseDTO> getAllDistrict() {
        return areaService.getAllDistrict();
    }
}
