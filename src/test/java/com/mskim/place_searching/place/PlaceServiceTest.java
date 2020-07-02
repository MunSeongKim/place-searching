package com.mskim.place_searching.place;

import com.mskim.place_searching.keyword.domain.Keyword;
import com.mskim.place_searching.keyword.repository.KeywordRepository;
import com.mskim.place_searching.place.dto.Place;
import com.mskim.place_searching.place.dto.PlaceDto;
import com.mskim.place_searching.place.service.PlaceService;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PlaceServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlaceService placeService;
    @Autowired
    private KeywordRepository keywordRepository;

    private String keyword;
    private int page;
    private int size;


    @BeforeEach
    void setUp() {
        keyword = "서울역";
        size = 10;
        page = 1;
    }

    @Test
    void PlaceService_장소_조회() {

        PlaceDto dto = placeService.retrievePlace(keyword, page, size);

        logger.info(() -> "Places: " + dto);

        then(dto.getPlaces()).isNotNull();
        then(dto.getPlaces().size()).isEqualTo(size);

        then(dto.getPager()).isNotNull();
        then(dto.getPager().getTotalItemCount()).isEqualTo(35);
        then(dto.getPager().getTotalPageCount()).isEqualTo(4);
    }

    @Test
    void PlaceService_장소_조회_검색어_저장() {
        placeService.retrievePlace(keyword, page, size);
        placeService.retrievePlace(keyword, page, size);
        keyword = "남대문";
        // page 초기화 작업 테스트
        PlaceDto dto = placeService.retrievePlace(keyword, page, size);

        then(dto.getPlaces()).isNotNull();
        then(dto.getPlaces().size()).isEqualTo(size);

        then(dto.getPager()).isNotNull();
        then(dto.getPager().getTotalItemCount()).isEqualTo(45);
        then(dto.getPager().getTotalPageCount()).isEqualTo(5);


        // 검색어 저장 여부 테스트
        List<Keyword> keywords = keywordRepository.findTop10ByOrderByCountDesc();

        for (Keyword keyword : keywords) {
            logger.info(() -> keyword.toString());
        }

        then(keywords).isNotNull();
        then(keywords.size()).isGreaterThanOrEqualTo(2);
        then(keywords.get(0).getValue()).isEqualTo("서울역");
        then(keywords.get(0).getCount()).isEqualTo(3);
        then(keywords.get(1).getValue()).isEqualTo("남대문");
        then(keywords.get(1).getCount()).isEqualTo(1);
    }

    @Test
    void PlaceService_장소_상세_조회() {
        keyword = "광화문";
        PlaceDto dto = placeService.retrievePlace(keyword, page, size);
        int placeId = dto.getPlaces().get(0).getId();
        Place place = placeService.retrievePlaceDetail(placeId);

        then(place).isNotNull();
        then(place.getId()).isEqualTo(placeId);
        then(place.getShortcutLink()).isNotBlank();

        logger.info(() -> place.toString());
    }

}