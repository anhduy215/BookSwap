package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Area.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID> {
    Optional<Area> findByCity(String city);
    @Query("SELECT DISTINCT a.city from Area a")
    List<String> findAllCity();
}
