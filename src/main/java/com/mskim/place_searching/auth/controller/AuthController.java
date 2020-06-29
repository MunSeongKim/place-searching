package com.mskim.place_searching.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/view/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/sign_in")
    public String signIn(@RequestParam(value = "error", required = false) Boolean error,
                         @RequestAttribute(value = "id", required = false) String id) {
        logger.info("-----> Error: " + error);
        logger.info("-------> ID: " + id);

        return "auth/sign_in";
    }
/*
    @PostMapping("/sign_in")
    public String signInForwarded(@RequestParam("id") String id) {
        logger.debug("----------> User: " + id);
        return "auth/sign_in";
    }*/
}
