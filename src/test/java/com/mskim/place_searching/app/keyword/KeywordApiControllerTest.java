package com.mskim.place_searching.app.keyword;

import com.mskim.place_searching.app.keyword.domain.Keyword;
import com.mskim.place_searching.app.keyword.service.KeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
class KeywordApiControllerTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JacksonTester<List<Keyword>> json;

    @Autowired
    MockMvc mvc;

    @Autowired
    KeywordService service;

    String placeName1 = "서울역";
    String placeName2 = "남대문";

    @BeforeEach
    void setUp() {
        service.storeKeyword(placeName1);
        service.storeKeyword(placeName1);
        service.storeKeyword(placeName2);
    }

    @Test
    @WithMockUser
    void KeywordController_인기_키워드_조회() throws Exception {
        MvcResult result = mvc.perform(get("/api/keywords/hot").characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        then(result.getResponse().getContentAsString()).isNotNull();

        List<Keyword> keywords = json.parseObject(result.getResponse().getContentAsString());

        logger.info(() -> keywords.toString());

        then(keywords).isNotNull();
        then(keywords.get(0).getValue()).isEqualTo(placeName1);
        then(keywords.get(0).getCount()).isEqualTo(2);
    }


}