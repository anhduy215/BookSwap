package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.common.Pagination;
import com.example.bookswapplatform.dto.AreaDTO;
import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.DistrictDTO;
import com.example.bookswapplatform.repository.DistrictRepository;
import com.example.bookswapplatform.service.AreaService;
import com.example.bookswapplatform.entity.Area.Area;
import com.example.bookswapplatform.entity.Area.District;
import com.example.bookswapplatform.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final DistrictRepository districtRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<BaseResponseDTO> getAllArea() {
        Set<AreaDTO> areaDTOS = new HashSet<>();
        for (Area area: areaRepository.findAll()
             ) {
            areaDTOS.add(convertToDTO(area));
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, areaDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getCity() {
        List<String> cities = areaRepository.findAllCity();
        List<AreaDTO> areaDTOS = new ArrayList<>();
        for (String city: cities
             ) {
            AreaDTO areaDTO = new AreaDTO();
            areaDTO.setCity(city);
            areaDTO.setDistrictDTOS(null);
            areaDTOS.add(areaDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, areaDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getDistrict(String city) {
        List<String> districts = districtRepository.findAllDistrict(city);
        List<DistrictDTO> districtDTOS = new ArrayList<>();
        for (String district: districts
             ) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setDistrict(district);
            districtDTOS.add(districtDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, districtDTOS));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllDistrict() {
        List<String> districts = districtRepository.findAllDistrictPage();
        List<DistrictDTO> districtDTOS = new ArrayList<>();
        for (String district: districts
        ) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setDistrict(district);
            districtDTOS.add(districtDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, districtDTOS));
    }


    public AreaDTO convertToDTO (Area area) {
        Set<DistrictDTO> districtDTOS = new HashSet<>();
        if(area == null) {
            return null;
        }
        AreaDTO areaDTO = modelMapper.map(area, AreaDTO.class);
        for (District district: area.getDistricts()
             ) {
            DistrictDTO districtDTO = modelMapper.map(district, DistrictDTO.class);
            districtDTOS.add(districtDTO);
        }

        areaDTO.setDistrictDTOS(districtDTOS);
        return areaDTO;
    }
}
