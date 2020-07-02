package com.mskim.place_searching.auth;

import com.mskim.place_searching.auth.controller.AuthController;
import com.mskim.place_searching.auth.repository.AuthRepository;
import com.mskim.place_searching.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
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
        final ResultActions actions = mockMvc.perform(get("/view/auth/sign_in").characterEncoding("UTF-8"))
                .andDo(print());

        actions.andExpect(status().isOk())
            .andExpect(model().attributeDoesNotExist("account"))
            .andExpect(model().attributeDoesNotExist("error_message"));
    }
}