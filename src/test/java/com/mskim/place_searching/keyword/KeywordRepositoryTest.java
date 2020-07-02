package com.mskim.place_searching.keyword;

import com.mskim.place_searching.keyword.domain.Keyword;
import com.mskim.place_searching.keyword.repository.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class KeywordRepositoryTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KeywordRepository keywordRepository;

    private Keyword keyword;
    private String placeName;

    @BeforeEach
    void setUp() {
        placeName = "서울역";
        keyword = Keyword.builder().value(placeName).build();
    }

    @Test
    void KeywordRepository_검색어_저장_조회_확인() {
        // given
        this.keywordRepository.save(keyword);

        // when
        final Keyword savedKeyword = this.keywordRepository.findByValue(placeName).orElse(null);
        logger.info(() -> savedKeyword.toString());

        // then
        then(savedKeyword).isNotNull();
        then(savedKeyword.getCount()).isEqualTo(1);
    }

    @Test
    void KeywordRepository_검색어_TOP_10_조회() {
        // given
        Keyword keyword;
        for (int i = 1; i <= 20; i++) {
            keyword = Keyword.builder().value(i+"").build();
            keyword = this.keywordRepository.save(keyword);
            for (int j = 1; j <= i; j++) {
                keyword.increaseCount();
            }
            this.keywordRepository.save(keyword);
        }

        // when
        List<Keyword> hotKeywords = this.keywordRepository.findTop10ByOrderByCountDesc();

        // then
        then(hotKeywords).isNotNull();
        then(hotKeywords.size()).isEqualTo(10);
        then(hotKeywords.get(0).getValue()).isEqualTo("20");

        for (Keyword item: hotKeywords) {
            logger.info(() -> item.toString());
        }
    }
}