package com.mskim.place_searching.runner;

import com.mskim.place_searching.auth.domain.Member;
import com.mskim.place_searching.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInitializationRunner implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(UserInitializationRunner.class);

    private final AuthService authService;

    @Autowired
    public UserInitializationRunner(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Member member = Member.builder()
                .account("chyin370")
                .password("test")
                .build();

        this.authService.signUp(member);

        logger.info("Initial member: " + member);
    }
}
