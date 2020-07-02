package com.mskim.place_searching.auth;

import com.mskim.place_searching.auth.domain.Member;
import com.mskim.place_searching.auth.repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class AuthRepositoryTest {
    private final AuthRepository authRepository;

    private Member member;

    @Autowired
    public AuthRepositoryTest(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

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
        final Member retrievedMember = this.authRepository.findByAccount(member.getAccount()).orElse(null);

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