package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.DistanceMatrix.*;
import com.example.bookswapplatform.service.DistanceMatrixService;
import com.example.bookswapplatform.service.GeocodingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DistanceMatrixServiceImpl implements DistanceMatrixService {
    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final GeocodingService geocodingService;
    @Override
    public Map<String, String> getDistanceMatrix(String origins, String destinations) {
        String originsPlaceId = geocodingService.getPlaceId(origins);
        System.out.println(originsPlaceId);
        String destinationsPlaceId = geocodingService.getPlaceId(destinations);
        System.out.println(destinationsPlaceId);
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?key=" + apiKey +
                "&origins=place_id:" + originsPlaceId +
                "&destinations=place_id:" + destinationsPlaceId;
        Map<String, String> result = new HashMap<>();

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            DistanceMatrixResponse response = objectMapper.readValue(jsonResponse, DistanceMatrixResponse.class);

            if ("OK".equals(response.getStatus())) {
                List<Row> rows = response.getRows();
                if (!rows.isEmpty()) {
                    Row firstRow = rows.get(0);
                    List<Element> elements = firstRow.getElements();
                    if (!elements.isEmpty()) {
                        Element firstElement = elements.get(0);
                        Distance distance = firstElement.getDistance();
                        Duration duration = firstElement.getDuration();

                        String distanceText = distance.getText();
                        String durationText = duration.getText();

                        // Put the values into the result map
                        result.put("distanceText", distanceText);
                        result.put("durationText", durationText);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
            // You might want to put default values or handle the error case
            result.put("distanceText", "Error");
            result.put("durationText", "Error");
        }

        return result;
    }
}
