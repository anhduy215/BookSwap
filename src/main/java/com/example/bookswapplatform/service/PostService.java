package com.example.bookswapplatform.service;

import com.example.bookswapplatform.common.FilterRequest;
import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.PostRequest;
import com.example.bookswapplatform.dto.PostUpdateBookRequest;
import com.example.bookswapplatform.dto.PostUpdateRequest;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface PostService {
    ResponseEntity<BaseResponseDTO> createPost (Principal principal, PostRequest postRequest);
    ResponseEntity<BaseResponseDTO> getUserActivePost (Principal principal);
    ResponseEntity<BaseResponseDTO> getUserDeactivePost(Principal principal);
    ResponseEntity<BaseResponseDTO> getUserLockedPost(Principal principal);
    ResponseEntity<BaseResponseDTO> filterPost(int pageNumber,
                                               int pageSize,
                                               String sortBy,
                                               String sortOrder,
                                               String keyWord,
                                               FilterRequest filterRequest);
    ResponseEntity<BaseResponseDTO> getPostDetail(UUID id);
    ResponseEntity<BaseResponseDTO> modifyPost (Principal principal, UUID postId, PostUpdateRequest postUpdateRequest);
    ResponseEntity<BaseResponseDTO> modifyBookInPost(Principal principal, UUID postId, PostUpdateBookRequest postUpdateBookRequest);
    ResponseEntity<BaseResponseDTO> deletePost (Principal principal, UUID postId);
}
