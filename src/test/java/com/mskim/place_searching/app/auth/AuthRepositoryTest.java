package com.mskim.place_searching.app.auth;

import com.mskim.place_searching.app.auth.domain.Member;
import com.mskim.place_searching.app.auth.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;


@DataJpaTest
class AuthRepositoryTest {
    @Autowired
    private AuthRepository authRepository;

    private Member member;


    @BeforeEach
    void setUp() {
        member = Member.builder()
                .account("test")
                .password("test1")
                .build();
    }

    @Test
    void AuthRepository_사용자_등록() {
        final Member savedMember = this.authRepository.save(member);

        then(savedMember).isNotNull();
        then(savedMember.getMemberId()).isNotZero().isGreaterThan(0);
        then(savedMember.getAccount()).isEqualTo("test");
        then(savedMember.getPassword()).isEqualTo("test1");
    }

    @Test
    void AuthRepository_사용자_조회() {
        // given
        this.authRepository.save(member);

        // when
        final Member retrievedMember = this.authRepository.findByAccount(member.getAccount()).orElse(null);

        // then
        then(retrievedMember).isNotNull();
        then(retrievedMember.getMemberId()).isNotZero().isGreaterThan(0);
        then(retrievedMember.getAccount()).isNotBlank().isEqualTo("test");
        then(retrievedMember.getPassword()).isNotBlank().isEqualTo("test1");
    }

    @Test
    void AuthRepository_유효하지_않은_사용자_조회() {
        final Member retrievedMember = this.authRepository.findByAccount(any(String.class)).orElse(null);

        then(retrievedMember).isNull();
    }
}