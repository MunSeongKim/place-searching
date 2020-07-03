package com.mskim.place_searching.app.place.controller;

import com.mskim.place_searching.app.keyword.service.KeywordService;
import com.mskim.place_searching.app.place.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"", "/", "/view/place"})
public class PlaceController {
    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("")
    public String index() {
        return "place/index";
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "query") String placeName,
                               @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(name = "size", required = false, defaultValue = "15") int size) {

        return new ModelAndView("place/index", "result_place", this.placeService.retrievePlace(placeName, page, size));
    }


}
