package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.*;
import com.example.bookswapplatform.entity.Post.Post;
import com.example.bookswapplatform.entity.SystemLog.Action;
import com.example.bookswapplatform.entity.SystemLog.Object;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.PostRepository;
import com.example.bookswapplatform.repository.UserRepository;
import com.example.bookswapplatform.service.PostServiceHelper;
import com.example.bookswapplatform.service.UserService;
import com.example.bookswapplatform.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PostServiceHelper postServiceHelper;
    private final PostRepository postRepository;
    private final SystemServiceImpl systemService;

    @Override
    public ResponseEntity<BaseResponseDTO> updateUser(Principal principal, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Cấu hình Condition để giữ nguyên giá trị khi giống hoặc là null
        Condition<?, ?> skipNullAndSameValue = ctx ->
                ctx.getSource() != null && !ctx.getSource().equals(ctx.getDestination());

        modelMapper.typeMap(UpdateUserRequest.class, User.class)
                .addMappings(mapper -> {
                    mapper.when(skipNullAndSameValue).map(UpdateUserRequest::getLastName, User::setLastName);
                    mapper.when(skipNullAndSameValue).map(UpdateUserRequest::getFirstName, User::setFirstName);
                    mapper.when(skipNullAndSameValue).map(UpdateUserRequest::getPhoneNum, User::setPhone);
                    mapper.when(skipNullAndSameValue).map(UpdateUserRequest::getIdCard, User::setIdCard);
                    mapper.when(skipNullAndSameValue).map(UpdateUserRequest::getImage, User::setImage);
                    mapper.when(skipNullAndSameValue).map(UpdateUserRequest::getGender, User::setGender);
                    mapper.skip(User::setDateOfBirth);
                });
        modelMapper.map(updateUserRequest, user);

        user.setUpdateBy(user.getEmail());

        if (updateUserRequest.getDateOfBirth() != null) {
            try {
                user.setDateOfBirth(DateTimeUtils.convertStringToLocalDate(updateUserRequest.getDateOfBirth()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        userRepository.save(user);
        systemService.saveSystemLog(user, Object.PROFILE, Action.UPDATE);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Update success"));

    }

    @Override
    public ResponseEntity<BaseResponseDTO> userProfile(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, convertToDTO(user)));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getUserInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        //List<Post> posts = postRepository.findByCreateBy(user);
        UserInfoDTO userInfoDTO = convertToUserInfoDTO(user);

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, userInfoDTO));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> reportUser() {
        return null;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPhoneNum(user.getPhone());
        UserWalletDTO userWalletDTO = modelMapper.map(user.getUserWallet(), UserWalletDTO.class);
        userDTO.setNumOfRate(user.getRates().size());
        userDTO.setUserWalletDTO(userWalletDTO);
        userDTO.setFireBaseId(user.getFireBaseUid());
        return userDTO;
    }

    public UserInfoDTO convertToUserInfoDTO(User user) {
        if (user == null) {
            return null;
        }
//        if(user.getOrdersList().isEmpty()) {
//            user.setOrdersList(null);
//
//        }
        int num = 0;
        UserInfoDTO userInfoDTO = modelMapper.map(user, UserInfoDTO.class);
        for (Post post : user.getPostList()
        ) {
            num++;
        }
        userInfoDTO.setFireBaseId(user.getFireBaseUid());
        userInfoDTO.setNumOfPost(num);
        userInfoDTO.setPhoneNum(user.getPhone());
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setNumOfRate(user.getRates().size());
        List<Post> posts = user.getPostList();
        List<PostGeneralDTO> postGeneralDTOS = new ArrayList<>();
        if (posts.isEmpty()) {
            userInfoDTO.setPostGeneralDTOs(null);
        } else {
            for (Post post : posts
            ) {
                PostGeneralDTO postGeneralDTO = postServiceHelper.convertToGeneralDTO(post);
                postGeneralDTOS.add(postGeneralDTO);
            }
            userInfoDTO.setPostGeneralDTOs(postGeneralDTOS);
        }
        return userInfoDTO;
    }
}
