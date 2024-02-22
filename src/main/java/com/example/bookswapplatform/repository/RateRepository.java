package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.User.Rate;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RateRepository extends JpaRepository<Rate, UUID> {
    List<Rate> findAllByUser (User user);
    List<Rate> findAllByCreateBy (User user);
}
