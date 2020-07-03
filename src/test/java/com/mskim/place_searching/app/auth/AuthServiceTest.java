package com.mskim.place_searching.app.auth;

import com.mskim.place_searching.app.auth.domain.Member;
import com.mskim.place_searching.app.auth.repository.AuthRepository;
import com.mskim.place_searching.app.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.BDDAssertions.then;


@DataJpaTest
@WebAppConfiguration
class AuthServiceTest {
    @Autowired
    ApplicationContext context;

    private final AuthService authService;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    private Member member;

    public AuthServiceTest(AuthRepository authRepository) {
        this.authService = context.getBean(AuthService.class);
        this.authRepository = context.getBean(AuthRepository.class);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .account("service_test1")
                .password("passwd")
                .build();
    }

    @Test
    void AuthService_회원_가입() {
        this.authService.signUp(member);

        final Member registeredMember = this.authRepository.findByAccount("service_test1").orElse(null);

        then(registeredMember).isNotNull();
        then(registeredMember.getMemberId()).isNotZero().isGreaterThan(0);
        then(registeredMember.getAccount()).isEqualTo("service_test1");
        then(this.passwordEncoder.matches("passwd", registeredMember.getPassword())).isTrue();
    }

    @Test
    void AuthService_초기_회원_등록_확인() {
        String targetAccount = "chyin370";

        final Member initialMember = this.authRepository.findByAccount(targetAccount)
                .orElse(null);

        then(initialMember).isNotNull();
        then(initialMember.getMemberId()).isNotZero().isGreaterThan(0);
        then(this.passwordEncoder.matches("test", initialMember.getPassword())).isTrue();
    }
}