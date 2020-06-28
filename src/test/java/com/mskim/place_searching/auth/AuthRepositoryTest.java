package com.mskim.place_searching.auth;

import com.mskim.place_searching.auth.domain.Member;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthRepositoryTest {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthRepositoryTest(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    void registerMemberTest() {
        // given
        Member member = Member.builder()
                .account("test")
                .password(this.passwordEncoder.encode("test1"))
                .build();

        // when
        member = this.authRepository.save(member);

        // then
        then(member).isNotNull();
        then(member.getMemberId()).isGreaterThan(0);
        then(member.getAccount()).isEqualTo("test");
        then(this.passwordEncoder.matches("test1", member.getPassword())).isTrue();
    }

    @Test
    void retrieveInitializedMemberTest() {
        // given
        String targetAccount = "chyin370";

        // when
        Member member = this.authRepository.findByAccount(targetAccount)
                .orElseThrow(() -> new UsernameNotFoundException(targetAccount));

        // then
        then(member.getMemberId()).isGreaterThan(0);
        // passwordEncoder.mathces("Confirm String", "Encoded String");
        then(this.passwordEncoder.matches("test", member.getPassword())).isTrue();
    }
}