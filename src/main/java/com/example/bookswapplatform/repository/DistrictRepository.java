package com.example.bookswapplatform.repository;

import com.example.bookswapplatform.entity.Area.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DistrictRepository extends JpaRepository<District, UUID> {
    Optional<District> findByDistrict(String district);
    @Query("SELECT DISTINCT d.district from District d where d.city.city= :city")
    List<String> findAllDistrict(String city);
    @Query("SELECT DISTINCT d.district from District d")
    List<String> findAllDistrictPage();
}
