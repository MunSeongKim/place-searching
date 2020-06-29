package com.mskim.place_searching.auth.service;

import com.mskim.place_searching.auth.domain.Member;
import com.mskim.place_searching.auth.AuthRepository;
import com.mskim.place_searching.configuration.security.SecurityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Optional;

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

    public ModelMap getModelFromSession(HttpSession session) {
        ModelMap model = new ModelMap();

        String usernameKey = SecurityConstant.SECURITY_USERNAME_KEY.getValue();
        String id = Optional.ofNullable((String) session.getAttribute(usernameKey))
                .orElse(null);
        model.addAttribute("account", id);

        Optional<Exception> exception = Optional.ofNullable((Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"));
        if (exception.isPresent()) {
            String errorMessage = exception.get().getLocalizedMessage();
            model.addAttribute("error_message", errorMessage);
        }

        return model;
    }
}
