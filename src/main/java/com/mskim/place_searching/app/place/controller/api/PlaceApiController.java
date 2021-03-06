package com.mskim.place_searching.app.place.controller.api;

import com.mskim.place_searching.app.place.dto.Place;
import com.mskim.place_searching.app.place.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/places", produces = "application/json;charset=UTF-8")
public class PlaceApiController {
    private final PlaceService placeService;

    @Autowired
    public PlaceApiController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public Place getOnePlace(@PathVariable("id") int placeId, HttpServletRequest request) {
        return this.placeService.retrievePlaceDetail(placeId, request.getSession());
    }
}
