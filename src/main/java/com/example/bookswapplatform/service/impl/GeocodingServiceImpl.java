package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.GeocodingResponse;
import com.example.bookswapplatform.dto.GeocodingResult;
import com.example.bookswapplatform.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeocodingServiceImpl implements GeocodingService {
    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public String getPlaceId(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;
        try {
            GeocodingResponse geocodingResponse = restTemplate.getForObject(url, GeocodingResponse.class);
            if (geocodingResponse != null && "OK".equals(geocodingResponse.getStatus())) {
                List<GeocodingResult> results = geocodingResponse.getResults();
                if (!results.isEmpty()) {
                    GeocodingResult firstResult = results.get(0);
                    return firstResult.getPlace_id();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
        return null;
    }
}
