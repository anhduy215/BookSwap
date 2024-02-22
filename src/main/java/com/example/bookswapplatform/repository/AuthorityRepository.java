package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Role.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

}
