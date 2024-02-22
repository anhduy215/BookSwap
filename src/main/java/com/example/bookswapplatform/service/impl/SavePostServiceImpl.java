package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.PostGeneralDTO;
import com.example.bookswapplatform.dto.SavePostDTO;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.SavedPost;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.PostRepository;
import com.example.bookswapplatform.repository.SavePostRepository;
import com.example.bookswapplatform.repository.UserRepository;
import com.example.bookswapplatform.service.PostServiceHelper;
import com.example.bookswapplatform.service.SavePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavePostServiceImpl implements SavePostService {
    private final UserRepository userRepository;
    private final SavePostRepository savePostRepository;
    private final PostRepository postRepository;
    private final PostServiceHelper postServiceHelper;
    @Override
    public ResponseEntity<BaseResponseDTO> savePost(Principal principal, UUID postId) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        SavedPost savedPost = new SavedPost();
        savedPost.setUserId(user.getId());
        savedPost.setPostId(postId);
        savePostRepository.save(savedPost);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success"));
    }


    @Override
    public ResponseEntity<BaseResponseDTO> getSavePost(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        List<SavedPost> savedPosts = savePostRepository.findAllByUserId(user.getId());
        SavePostDTO savePostDTO = new SavePostDTO();
        List<PostGeneralDTO> postGeneralDTOs = new ArrayList<>();
        if(savedPosts.isEmpty()) {
            savePostDTO = null;
        } else {
            for (SavedPost savePost: savedPosts
                 ) {
                Post post = postRepository.findIncludeDeletedPost(savePost.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post not found!"));
                postGeneralDTOs.add(postServiceHelper.convertToGeneralDTO(post));

            }
            savePostDTO.setPostGeneralDTOs(postGeneralDTOs);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success",null, savePostDTO));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> removePost(UUID postId) {
        SavedPost savedPost = savePostRepository.findByPostId(postId);
        savePostRepository.delete(savedPost);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success"));
    }
}
