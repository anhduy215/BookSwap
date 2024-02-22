package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.RateDTO;
import com.example.bookswapplatform.dto.RateRequestDTO;
import com.example.bookswapplatform.entity.Order.Orders;
import com.example.bookswapplatform.entity.User.Rate;
import com.example.bookswapplatform.entity.User.User;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.OrderRepository;
import com.example.bookswapplatform.repository.RateRepository;
import com.example.bookswapplatform.repository.UserRepository;
import com.example.bookswapplatform.service.PostServiceHelper;
import com.example.bookswapplatform.service.RateService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final PostServiceHelper postServiceHelper;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<BaseResponseDTO> rateUser(Principal principal, UUID orderId, RateRequestDTO rateRequestDTO) {

        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        User userRate = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        User userGetRate = orders.getCreateBy();
        if (userRate.getId().equals(userGetRate.getId())) {
            userGetRate = orders.getPost().getCreateBy();
        } else {
            userGetRate = orders.getCreateBy();
        }

        Rate rate = new Rate();
        rate.setRateNumber(rateRequestDTO.getRateNumber());
        if (rateRequestDTO.getDescription() == null) {
            rate.setDescription(null);
        } else {
            rate.setDescription(rateRequestDTO.getDescription());
        }

        rate.setCreateBy(userRate);
        rate.setUser(userGetRate);

        //them orderId de xu ly oder lay post
        rate.setOrders(orders);
        rateRepository.save(rate);
        userGetRate.setTotalRate(calculateTotalRate(userGetRate));
        userRepository.save(userGetRate);
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success"));
    }

    public float calculateTotalRate(User user) {
        float totalRate = 0;
        int sum = 0;

        for (Rate userRate : user.getRates()) {
            sum += userRate.getRateNumber();
        }

        if (!user.getRates().isEmpty()) {
            int num = user.getRates().size();
            totalRate = (float) sum / num;
            // Làm tròn đến số thập phân đầu tiên
            totalRate = (float) (Math.round(totalRate * 10.0) / 10.0);
        }
        return totalRate;
    }

    @Override
    public ResponseEntity<BaseResponseDTO> viewRate(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        List<Rate> rates = rateRepository.findAllByUser(user);
        List<RateDTO> rateDTOS = new ArrayList<>();
        for (Rate rate : rates
        ) {
            RateDTO rateDTO = convertToDTO(rate);
            rateDTOS.add(rateDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, rateDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> viewRateByCreateBy(Principal principal) {
        User user = userRepository.findByFireBaseUid(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        List<Rate> rates = rateRepository.findAllByCreateBy(user);
        List<RateDTO> rateDTOS = new ArrayList<>();
        for (Rate rate : rates
        ) {
            RateDTO rateDTO = convertToDTO(rate);
            rateDTOS.add(rateDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, rateDTOS));
    }


    @Override
    public ResponseEntity<BaseResponseDTO> viewOtherUserRate(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        List<Rate> rates = rateRepository.findAllByUser(user);
        List<RateDTO> rateDTOS = new ArrayList<>();
        for (Rate rate : rates
        ) {
            RateDTO rateDTO = convertToDTO(rate);
            rateDTOS.add(rateDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, rateDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> viewOtherUserRateCreate(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        List<Rate> rates = rateRepository.findAllByCreateBy(user);
        List<RateDTO> rateDTOS = new ArrayList<>();
        for (Rate rate : rates
        ) {
            RateDTO rateDTO = convertToDTO(rate);
            rateDTOS.add(rateDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Success", null, rateDTOS));
    }

    public RateDTO convertToDTO(Rate rate) {
        if (rate == null) {
            return null;
        }
        RateDTO rateDTO = modelMapper.map(rate, RateDTO.class);

        User user = rate.getCreateBy();
        String name = user.getLastName() + " " + user.getFirstName();
        rateDTO.setName(name);
        rateDTO.setImgUrl(user.getImage());
        Orders orders = rate.getOrders();
        if (orders == null) {
            rateDTO.setPostGeneralDTO(null);
        } else {
            rateDTO.setPostGeneralDTO(postServiceHelper.convertToGeneralDTO(orders.getPost()));
        }
        return rateDTO;

    }
}
