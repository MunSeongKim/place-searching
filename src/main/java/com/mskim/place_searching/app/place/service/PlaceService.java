package com.mskim.place_searching.app.place.service;

import com.mskim.place_searching.app.keyword.service.KeywordService;
import com.mskim.place_searching.app.place.dto.Place;
import com.mskim.place_searching.app.place.dto.PlaceDto;
import com.mskim.place_searching.app.place.support.client.KakaoMapSearchRestClient;
import com.mskim.place_searching.app.place.support.page.PlacePager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PlaceService {
    public static final String PLACE_CACHE_NAME = "place";
    public static final String PLACES_CACHE_NAME = "places";

    private static final String SHORTCUT_URL_PREFIX = "https://map.kakao.com/link/map/";

    private final CacheManager cacheManager;
    private final KakaoMapSearchRestClient client;
    private final KeywordService keywordService;

    private boolean isNewKeyword;
    private String keyword;
    private PlacePager placePager;

    @Autowired
    public PlaceService(CacheManager cacheManager, KakaoMapSearchRestClient client, KeywordService keywordService) {
        this.cacheManager = cacheManager;
        this.client = client;
        this.keywordService = keywordService;
        this.isNewKeyword = true;
    }

    public Place retrievePlaceDetail(int placeId) {
        Place place = cacheManager.getCache(PLACE_CACHE_NAME)
                                        .get(placeId, Place.class);

        if (place == null) {
            throw new RuntimeException("PlaceId(" + placeId + ") not fount.");
        }

        return place;
    }

    public PlaceDto retrievePlace(String placeName, int page, int size) {
        List<Place> places = generatePlace(placeName, page, size);
        this.placePager = updatePager(placeName, page, size);

        int itemIndex = placePager.getStartItemNumber();
        for (Place place : places) {
            place.assignItemIndex(itemIndex++);
        }

        return PlaceDto.builder()
                .places(places)
                .pager(this.placePager)
                .build();
    }

    public List<Place> generatePlace(String placeName, int page, int size) {
        this.updateKeywordState(placeName);

        String cacheItemKey = placeName + "_" + page;
        Cache cache = cacheManager.getCache(PLACES_CACHE_NAME);
        List<Place> places = cache.get(cacheItemKey, List.class);

        if (places != null) {
            return places;
        }

        MultiValueMap params = createParams(placeName, page, size);
        Map response = (Map) client.setParams(params).getListAsEntity();

        places = parsePlaceData(response);
        cache.put(cacheItemKey, places);

        return places;
    }

    private List<Place> parsePlaceData(Map response) {
        List<Map<String, String>> placeList = (List) response.get("documents");

        List<Place> places = new ArrayList<>();
        for (Map<String, String> item: placeList) {
            int id = Integer.parseInt(item.get("id"));
            Place place = Place.builder()
                    .id(id)
                    .name(item.get("place_name"))
                    .category(item.get("category_name"))
                    .address(item.get("address_name"))
                    .roadAddress(item.get("road_address_name"))
                    .phone(item.get("phone"))
                    .longitude(Double.valueOf(item.get("x")))
                    .latitude(Double.valueOf(item.get("y")))
                    .shortcutLink(SHORTCUT_URL_PREFIX + id)
                    .build();
            cacheManager.getCache(PLACE_CACHE_NAME).put(id, place);
            places.add(place);
        }

        return places;
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
        this.keywordService.storeKeyword(newKeyword);
    }

    private MultiValueMap createParams(String placeName, Integer page, Integer size) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", placeName);
        params.add("page", page.toString());
        params.add("size", size.toString());

        return params;
    }

}
