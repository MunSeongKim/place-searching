package com.mskim.place_searching.app.place;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PlaceControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void PlaceController_검색_페이지() throws Exception {
        mvc.perform(get("/view/place").characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("place/index"))
                .andReturn();
    }

    @Test
    @WithMockUser
    void PlaceController_검색_실행() throws Exception {
        mvc.perform(get("/view/place/search")
                .characterEncoding("UTF-8")
                .queryParam("query", "서울역")
                .queryParam("page", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("place/index"))
            .andExpect(model().attributeExists("result_place"))
            .andReturn();
    }

    @Test
    @WithMockUser
    void PlaceController_검색_2페이지_실행_() throws Exception {
        mvc.perform(get("/view/place/search")
                .characterEncoding("UTF-8")
                .queryParam("query", "서울역")
                .queryParam("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("place/index"))
                .andExpect(model().attributeExists("result_place"))
                .andReturn();
    }
}