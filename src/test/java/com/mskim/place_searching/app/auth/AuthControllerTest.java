package com.mskim.place_searching.app.auth;

import com.mskim.place_searching.app.auth.controller.AuthController;
import com.mskim.place_searching.app.auth.repository.AuthRepository;
import com.mskim.place_searching.app.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @Test
    void AuthController_로그인_페이지() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/view/auth/sign_in").characterEncoding("UTF-8"))
                .andDo(print());

        actions.andExpect(status().isOk())
            .andExpect(model().attributeDoesNotExist("error_message"));
    }
}