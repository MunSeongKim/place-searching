package com.mskim.place_searching.place.service;

import com.mskim.place_searching.place.dto.Place;
import com.mskim.place_searching.place.dto.PlaceDto;
import com.mskim.place_searching.place.support.client.KakaoMapSearchRestClient;
import com.mskim.place_searching.place.support.page.PlacePager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    private final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    private final KakaoMapSearchRestClient client;

    private boolean isNewKeyword;
    private String keyword;
    private PlacePager placePager;

    @Autowired
    public PlaceService(KakaoMapSearchRestClient client) {
        this.client = client;
        this.isNewKeyword = true;
    }

    public PlaceDto retrievePlace(String placeName, int page, int size) {
        updateKeywordState(placeName);

        MultiValueMap params = createParams(placeName, page, size);
        Map response = (Map) client.setParams(params).getListAsEntity();

        this.placePager = updatePager(placeName, page, size);

        return PlaceDto.builder()
                .places(parsePlaceData(response))
                .pager(this.placePager)
                .build();
    }

    private List<Place> parsePlaceData(Map response) {
        List<Map<String, String>> placeList = (List) response.get("documents");

        return placeList.stream()
                .map((item) -> Place.builder()
                        .id(item.get("id"))
                        .name(item.get("place_name"))
                        .address(item.get("address_name"))
                        .roadAddress(item.get("road_address_name"))
                        .phone(item.get("phone"))
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, Object> parseMetadata(Map response) {
        return (Map) response.get("meta");
    }

    private PlacePager updatePager(String keyword, int page, int size) {
        if (isNewKeyword) {
            return initializePager(keyword, size);
        }

        return this.placePager.update(page);
    }

    private PlacePager initializePager(String keyword, int size) {
        MultiValueMap params = createParams(keyword, PlacePager.MAX_PAGE_COUNT, size);

        // new pager
        Map<String, Object> response = (Map) client.setParams(params).getListAsEntity();
        Map<String, Object> meta = parseMetadata(response);

        logger.info("=========> " + meta.get("pageable_count"));
        Integer totalItemCount = (Integer) meta.get("pageable_count");

        return PlacePager.builder()
                .displayItemCount(size)
                .totalItemCount(totalItemCount)
                .build().update();
    }

    private void updateKeywordState(String newKeyword) {
        if (newKeyword.equals(keyword)) {
            isNewKeyword = false;
            return;
        }

        keyword = newKeyword;
        isNewKeyword = true;
    }

    private MultiValueMap createParams(String placeName, Integer page, Integer size) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", placeName);
        params.add("page", page.toString());
        params.add("size", size.toString());

        return params;
    }

}
