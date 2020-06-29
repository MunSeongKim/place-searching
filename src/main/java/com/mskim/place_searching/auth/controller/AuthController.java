package com.mskim.place_searching.auth.controller;

import com.mskim.place_searching.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/view/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/sign_in")
    public ModelAndView signIn(@RequestParam(value = "error", required = false) Boolean error,
                         HttpServletRequest request) {
        ModelMap model = authService.getModelFromSession(request.getSession());

        return new ModelAndView("auth/sign_in", model);
    }
}
