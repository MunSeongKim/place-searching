package com.mskim.place_searching.place.service;

import com.mskim.place_searching.place.dto.Place;
import com.mskim.place_searching.place.dto.PlaceDto;
import com.mskim.place_searching.place.support.client.KakaoMapSearchRestClient;
import com.mskim.place_searching.place.support.page.PlacePager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    public static final String CACHE_NAME = "place";

    private static final String SHORTCUT_URL_PREFIX = "https://map.kakao.com/link/map/";

    private final CacheManager cacheManager;
    private final KakaoMapSearchRestClient client;

    private boolean isNewKeyword;
    private String keyword;
    private PlacePager placePager;

    @Autowired
    public PlaceService(CacheManager cacheManager, KakaoMapSearchRestClient client) {
        this.cacheManager = cacheManager;
        this.client = client;
        this.isNewKeyword = true;
    }

    public Place retrievePlaceDetail(int placeId) {
        Place place = cacheManager.getCache(CACHE_NAME)
                                        .get(placeId, Place.class);

        if (place == null) {
            throw new RuntimeException("PlaceId(" + placeId + ") not fount.");
        }

        return place;
    }

    @Cacheable(value = "places", key = "#placeName + #page")
    public PlaceDto retrievePlace(String placeName, int page, int size) {
        this.updateKeywordState(placeName);

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
                .map((item) -> {
                    int id = Integer.parseInt(item.get("id"));
                    Place place = Place.builder()
                            .id(id)
                            .name(item.get("place_name"))
                            .address(item.get("address_name"))
                            .roadAddress(item.get("road_address_name"))
                            .phone(item.get("phone"))
                            .shortcutLink(SHORTCUT_URL_PREFIX + id)
                            .build();
                    cacheManager.getCache(CACHE_NAME).put(id, place);
                    return place;
                })
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
