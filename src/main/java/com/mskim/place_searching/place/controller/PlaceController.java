package com.mskim.place_searching.place.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"", "/", "/view/place"})
public class PlaceController {

    @GetMapping("")
    public String index() {
        return "layout/index";
    }

}
