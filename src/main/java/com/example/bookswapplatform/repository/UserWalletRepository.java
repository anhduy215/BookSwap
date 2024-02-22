package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Payment.UserWallet;
import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserWalletRepository extends JpaRepository<UserWallet, UUID> {
    Optional<UserWallet> getUserWalletByCreateBy (User user);
}
