package com.mskim.place_searching;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Integration Test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class PlaceDtoSearchingApplicationTests {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	void 서비스_접속_미인증_리다리엑션() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrlPattern("**/view/auth/sign_in"))
				.andDo(print());
	}

	@Test
	void 로그인() throws Exception {
		mockMvc.perform(formLogin("/auth/validation")
							.user("id", "chyin370")
							.password("password", "test"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated())
				.andDo(print());
	}

	@Test
	void 로그아웃() throws Exception {
		mockMvc.perform(logout("/auth/sign_out"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/view/auth/sign_in"))
				.andExpect(unauthenticated())
				.andDo(print());
	}

}
