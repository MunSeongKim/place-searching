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
}