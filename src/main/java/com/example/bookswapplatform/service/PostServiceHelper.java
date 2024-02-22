package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.PostDTO;
import com.example.bookswapplatform.dto.PostGeneralDTO;
import com.example.bookswapplatform.entity.Post.Post;

public interface PostServiceHelper {
    PostGeneralDTO convertToGeneralDTO(Post post);

    PostDTO convertToDTO(Post post);
}
