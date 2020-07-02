package com.mskim.place_searching.place.controller;

import com.mskim.place_searching.keyword.service.KeywordService;
import com.mskim.place_searching.place.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
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
    private final PlaceService placeService;
    private final KeywordService keywordService;

    @Autowired
    public PlaceController(PlaceService placeService, KeywordService keywordService) {
        this.placeService = placeService;
        this.keywordService = keywordService;
    }

    @GetMapping("")
    public String index() {
        return "place/index";
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(name = "query", required = false) String placeName,
                               @RequestParam(name = "page", required = false) int page,
                               @RequestParam(name = "size", required = false) int size) {

        this.keywordService.storeKeyword(placeName);
        return new ModelAndView("place/index", "places", this.placeService.retrievePlace(placeName, page, size));
    }


}
