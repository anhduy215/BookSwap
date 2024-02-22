package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAllCategory () {
        return categoryService.getAllCategory();

    }
    @Operation(description = "Đây là api để lấy thể loại chính")
    @GetMapping("/all/main-category")
    public ResponseEntity<BaseResponseDTO> getMainCategory () {
        return categoryService.getCategory();
    }
    @Operation(description = "Đây là api để lấy thể loại cụ thể")
    @GetMapping("/sub-category")
    public ResponseEntity<BaseResponseDTO> getSubCategory(@RequestParam String category) {
        return categoryService.getSubCategory(category);
    }
    @Operation(description = "Đây là api để lấy thể loại chi tiết")
    @GetMapping("/sub-sub-category")
    public ResponseEntity<BaseResponseDTO> getSubSubCategory(@RequestParam String subCategory) {
        return categoryService.getSubSubCategory(subCategory);
    }

    @GetMapping("/all/sub-category")
    public ResponseEntity<BaseResponseDTO> getAllSubCategory() {
        return categoryService.getAllSubCategory();
    }

    @GetMapping("/all/sub-sub-category")
    public ResponseEntity<BaseResponseDTO> getAllSubSubCategory() {
        return categoryService.getAllSubSubCategory();
    }
}
