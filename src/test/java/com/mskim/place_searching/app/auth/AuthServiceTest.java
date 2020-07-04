package com.mskim.place_searching.app.auth;

import com.mskim.place_searching.app.auth.domain.Member;
import com.mskim.place_searching.app.auth.repository.AuthRepository;
import com.mskim.place_searching.app.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AuthService.class)
@Transactional
class AuthServiceTest{
    @Autowired
    private AuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .account("service_test1")
                .password("passwd")
                .build();
    }

    @Test
    void AuthService_회원_가입_및_조회() {
        // given
        when(authRepository.save(member))
                .thenReturn(
                        member.changePassword(passwordEncoder.encode(member.getPassword()))
                );

        // when
        boolean result = this.authService.signUp(member);

        // then
        assertTrue(result);
    }


}