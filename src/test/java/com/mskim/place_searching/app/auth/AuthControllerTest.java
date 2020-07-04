package com.mskim.place_searching.app.auth;

import com.mskim.place_searching.app.auth.controller.AuthController;
import com.mskim.place_searching.app.auth.repository.AuthRepository;
import com.mskim.place_searching.app.auth.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @Test
    void AuthController_로그인_페이지() throws Exception {
        // given

        // when
        mockMvc.perform(get("/view/auth/sign_in"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/sign_in"))
                .andExpect(model().attributeDoesNotExist("error_message"))
                .andReturn();

        // then

    }

    @Test
    void AuthController_에러시_로그인_페이지_() throws Exception {
        // given
        when(authService.getModelFromSession(new MockHttpSession()))
                .thenReturn(new ModelMap("error_message", "ID/Password invalid."));

        // when
        final MvcResult result = mockMvc.perform(get("/view/auth/sign_in?error=true"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/sign_in"))
                .andExpect(model().attributeExists("error_message"))
                .andReturn();

        // then
        Assertions.assertThat(result.getModelAndView().getModel().get("error_message"));
    }
}