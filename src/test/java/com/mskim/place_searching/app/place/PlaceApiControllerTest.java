package com.mskim.place_searching.app.place;

import com.mskim.place_searching.app.place.dto.Place;
import com.mskim.place_searching.app.place.service.PlaceService;
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
class PlaceApiControllerTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JacksonTester<Place> json;

    @Autowired
    MockMvc mvc;

    @Autowired
    PlaceService service;

    String placeName1 = "서울역";
    String placeName2 = "남대문";
    int page = 1;
    int placeId = 9113903;

    @BeforeEach
    void setUp() {
        service.retrievePlace(placeName1, page);
        service.retrievePlace(placeName1, page);
        service.retrievePlace(placeName2, page);
    }

    @Test
    @WithMockUser
    void PlaceApiController_장소_상세_조회() throws Exception {
        MvcResult result = mvc.perform(get("/api/places/" + placeId).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        then(result.getResponse().getContentAsString()).isNotNull();

        Place place = json.parseObject(result.getResponse().getContentAsString());

        logger.info(() -> "Place >>> " + place.toString());

        then(place).isNotNull();
        then(place.getId()).isEqualTo(placeId);
        then(place.getName()).isEqualTo(placeName1);
        then(place.getShortcutLink()).isNotBlank();
    }

}