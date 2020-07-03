package com.mskim.place_searching.app.keyword;

import com.mskim.place_searching.app.keyword.domain.Keyword;
import com.mskim.place_searching.app.keyword.repository.KeywordRepository;
import com.mskim.place_searching.app.keyword.service.KeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KeywordServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KeywordService service;
    @Autowired
    private KeywordRepository repository;

    private String placeName1;
    private String placeName2;

    @BeforeEach
    void setUp() {
        placeName1 = "서울역";
        placeName2 = "남대문";
    }

    @Test
    void KeywordService_검색어_저장_있으면_수정() {
        service.storeKeyword(placeName1);
        service.storeKeyword(placeName1);
        service.storeKeyword(placeName2);

        List<Keyword> keywords = repository.findTop10ByOrderByCountDesc();

        for (Keyword keyword : keywords) {
            logger.info(() -> keyword.toString());
        }

        then(keywords).isNotNull();
        then(keywords.size()).isEqualTo(2);
        then(keywords.get(0).getValue()).isEqualTo(placeName1);
        then(keywords.get(0).getCount()).isEqualTo(2);
        then(keywords.get(1).getValue()).isEqualTo(placeName2);
        then(keywords.get(1).getCount()).isEqualTo(1);
    }

}