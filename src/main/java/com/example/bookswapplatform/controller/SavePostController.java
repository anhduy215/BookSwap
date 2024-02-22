package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.service.SavePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/save-post")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class SavePostController {
    private final SavePostService savePostService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponseDTO> savePost(Principal principal, @RequestParam UUID postId) {
        return savePostService.savePost(principal, postId);
    }
    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAll (Principal principal) {
        return savePostService.getSavePost(principal);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<BaseResponseDTO> remove (@RequestParam UUID postId) {
        return savePostService.removePost(postId);
    }

}
