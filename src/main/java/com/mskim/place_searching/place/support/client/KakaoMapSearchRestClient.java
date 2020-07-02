package com.mskim.place_searching.place.support.client;

import com.mskim.place_searching.support.rest.client.RestClientTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoMapSearchRestClient extends RestClientTemplate {
    private static final String KAKAO_SEARCH_PLACE_PATH = "/v2/local/search/keyword.json";

    public KakaoMapSearchRestClient(@Value("${kakao.client.key}") String kakaoClientKey,
                                    @Value("${kakao.domain.map}") String kakaoMapDomain) {
        super();

        this.setDomainUrl(kakaoMapDomain)
            .setPath(KAKAO_SEARCH_PLACE_PATH)
            .setHeaders(Map.of("Content-type", "application/json;charset=UTF-8",
                                "Authorization", "KakaoAK " + kakaoClientKey));
    }

    @Override
    protected Map parseListAsEntity(ResponseEntity<Map> response) {
        Map<String, Object> result = response.getBody();

        return result;
    }

}
