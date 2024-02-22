package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String userName);

    Optional<User> findByFireBaseUid(String uid);
    @Query("SELECT u.id from User u where u.fireBaseUid = :uid")
    UUID getUUIDByFireBaseUid(String uid);
    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE u.lastName like %:keyWord% or u.firstName like %:keyWord% or " +
            "u.email like %:keyWord% or u.phone like %:keyWord% or u.locationDetail like %:keyWord%")
    Page<User> searchByKeyWord(String keyWord, Pageable pageable);
    @Query("SELECT DISTINCT u FROM User u JOIN u.role r WHERE r.name = :role")
    Page<User> searchByRole(String role, Pageable pageable);

}
