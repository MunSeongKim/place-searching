package com.mskim.place_searching;

import com.mskim.place_searching.app.keyword.domain.Keyword;
import com.mskim.place_searching.app.place.controller.PlaceController;
import com.mskim.place_searching.app.place.dto.Place;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaceSearchingApplicationTests {
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private JacksonTester<Place> placeJson;

	@Autowired
	private JacksonTester<List<Keyword>> keywordsJson;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	@Order(1)
	void 서비스_접속_미인증_리다리엑션() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrlPattern("**/view/auth/sign_in"))
				.andDo(print());
	}

	@Test
	@Order(2)
	void 로그인() throws Exception {
		mockMvc.perform(formLogin("/auth/validation")
							.user("id", "munseong.kim")
							.password("password", "test"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated())
				.andDo(print());
	}

	@Test
	@WithMockUser
	@Order(3)
	void 장소_검색_페이지_요청() throws Exception {
		mockMvc.perform(get("/view/place").characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(view().name("place/index"))
				.andReturn();
	}

	@Test
	@WithMockUser
	@Order(4)
	void 장소_검색_실행() throws Exception {
		mockMvc.perform(get("/view/place/search")
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
	@Order(5)
	void 장소_상세_조회() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/places/9113903").characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn();

		then(result.getResponse().getContentAsString()).isNotNull();

		Place place = placeJson.parseObject(result.getResponse().getContentAsString());

		then(place).isNotNull();
		then(place.getId()).isEqualTo(9113903);
		then(place.getName()).isEqualTo("서울역");
		then(place.getShortcutLink()).isNotBlank();
	}

	@Test
	@WithMockUser
	@Order(6)
	void 인기_키워드_조회() throws Exception {
		// when
		MvcResult result = mockMvc.perform(get("/api/keywords/hot").characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		// then
		then(result.getResponse().getContentAsString()).isNotNull();

		List<Keyword> keywords = keywordsJson.parseObject(result.getResponse().getContentAsString());

		then(keywords).isNotNull();
		then(keywords.get(0).getValue()).isEqualTo("서울역");
		then(keywords.get(0).getCount()).isGreaterThanOrEqualTo(1);
	}

	@Test
	@WithMockUser
	@Order(7)
	void 장소_결과_페이지_이동() throws Exception {
		mockMvc.perform(get("/view/place/search")
				.characterEncoding("UTF-8")
				.queryParam("query", "서울역")
				.queryParam("page", "2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(view().name("place/index"))
				.andExpect(model().attributeExists("result_place"))
				.andReturn();
	}

	@Test
	@Order(8)
	void 로그아웃() throws Exception {
		mockMvc.perform(logout("/auth/sign_out"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/view/auth/sign_in"))
				.andExpect(unauthenticated())
				.andDo(print());
	}

}
