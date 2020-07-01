package com.mskim.place_searching.place;

import com.mskim.place_searching.configuration.restapi.client.support.KakaoRestTemplate;
import com.mskim.place_searching.place.dto.Places;
import com.mskim.place_searching.place.service.PlaceService;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PlaceServiceTest {
    private final Logger logger = LoggerFactory.getLogger(PlaceServiceTest.class);
    @Autowired
    private KakaoRestTemplate client;

    @Autowired
    private PlaceService placeService;

    @Value("${kakao.domain.map}")
    private String kakaoMapDomain;


    @Test
    void PlaceService_RestTemplate_확인() {
        String endpointUrlWithParams = UriComponentsBuilder.fromHttpUrl(kakaoMapDomain)
                .path("/v2/local/search/keyword.json")
                .queryParam("query", "서울역")
                .build().toUriString();
        logger.info(() -> "URL: " + endpointUrlWithParams);

        ResponseEntity<Map> response = client.getForEntity(endpointUrlWithParams, Map.class);
        Map<String, Object> result = response.getBody();
        logger.info(() -> result.toString());
        then(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        then(result.isEmpty()).isFalse();
        then(result.get("documents")).isInstanceOf(List.class);
        List<Map<String, Object>> documents = (List) result.get("documents");
        then(documents.size()).isGreaterThanOrEqualTo(1);
        then(documents.get(0)).isInstanceOf(Map.class);
        Map<String, Object> placeItem = documents.get(0);
        logger.info(() -> placeItem.toString());
        then(placeItem.get("place_name")).isEqualTo("서울역");
    }

    @Test
    void PlaceService_장소_조회() {
        Places places = placeService.retrievePlace("서울역");

        logger.info(() -> "Places: " + places);

        then(places).isNotEmpty();
        then(places.size()).isEqualTo(15);
        then(places.get(0).getName()).isEqualTo("서울역");
    }

}