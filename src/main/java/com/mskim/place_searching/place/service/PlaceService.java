package com.mskim.place_searching.place.service;

import com.mskim.place_searching.configuration.restapi.client.support.KakaoRestTemplate;
import com.mskim.place_searching.place.dto.Place;
import com.mskim.place_searching.place.dto.Places;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class PlaceService {
    private final Logger logger = LoggerFactory.getLogger(PlaceService.class);
    private static final String KAKAO_SEARCH_PLACE_PATH = "/v2/local/search/keyword.json";

    private final KakaoRestTemplate kakaoRestTemplate;

    @Value("${kakao.domain.map}")
    private String kakaoMapDomain;

    @Autowired
    public PlaceService(KakaoRestTemplate kakaoRestTemplate) {
        this.kakaoRestTemplate = kakaoRestTemplate;
    }

    public Places retrievePlace(String placeName) {
        String endpointUrlWithParams = UriComponentsBuilder.fromHttpUrl(kakaoMapDomain)
                .path(KAKAO_SEARCH_PLACE_PATH)
                .queryParam("query", placeName)
                .build().toUriString();

        ResponseEntity<Map> response = kakaoRestTemplate.getForEntity(endpointUrlWithParams, Map.class);
        Map<String, Object> result = response.getBody();
        List<Map<String, Object>> placeList = (List) result.get("documents");

        Places places = new Places();
        placeList.stream().forEach((item) -> {
            String itemPlaceName = (String) item.get("place_name");
            places.add(Place.builder().name(itemPlaceName).build());
        });

        return places;
    }

}
