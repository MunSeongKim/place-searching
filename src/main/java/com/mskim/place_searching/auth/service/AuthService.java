package com.mskim.place_searching.auth.service;

import com.mskim.place_searching.auth.domain.Member;
import com.mskim.place_searching.auth.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthRepository repository, PasswordEncoder passwordEncoder) {
        this.authRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean signUp(Member member) {
        member.changePassword(this.passwordEncoder.encode(member.getPassword()));
        return this.authRepository.save(member) != null ? true : false;
    }
}
