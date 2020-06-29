package com.mskim.place_searching.keyword.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/view/keyword")
public class KeywordController {
    
    @GetMapping(value="")
    public String index() {
        return "error/403";
    }
    
}
