package com.mskim.place_searching.app.auth;

import com.mskim.place_searching.app.auth.controller.AuthController;
import com.mskim.place_searching.app.auth.repository.AuthRepository;
import com.mskim.place_searching.app.auth.service.AuthService;
import com.mskim.place_searching.app.keyword.service.KeywordService;
import com.mskim.place_searching.app.place.controller.PlaceController;
import com.mskim.place_searching.app.place.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@WebMvcTest({AuthController.class, PlaceController.class})
@ContextConfiguration
@WebAppConfiguration
class SecurityConfigureTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AuthService authService;
    @MockBean
    private AuthRepository authRepository;
    @MockBean
    private PlaceService placeService;
    @MockBean
    private KeywordService keywordService;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void SecurityConfigure_로그인_페이지로_리다이렉션() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/").characterEncoding("UTF-8"))
                .andDo(print());

        actions.andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/view/auth/sign_in"));
    }

    @Test
    void SecurityConfigure_로그인_실패() throws Exception {
        final ResultActions actions = mockMvc.perform(formLogin("/auth/validation")
                                                        .user("id", "chyin370")
                                                        .password("password", "test"))
                .andDo(print());

        actions.andExpect(status().isFound())
                .andExpect(redirectedUrl("/view/auth/sign_in?error=true"));
    }

    @Test
    void SecurityConfigure_로그아웃() throws Exception {
        final ResultActions actions = mockMvc.perform(logout("/auth/sign_out"))
                .andDo(print());

        actions.andExpect(status().isFound())
            .andExpect(redirectedUrl("/view/auth/sign_in"));
    }

    @Test
    @WithMockUser
    void SecurityConfigure_인증된_후_페이지_접속() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/").characterEncoding("UTF-8"))
                .andDo(print());

        actions.andExpect(status().isOk());
    }
}