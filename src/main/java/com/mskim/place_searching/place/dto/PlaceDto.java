package com.mskim.place_searching.place.dto;

import com.mskim.place_searching.place.support.page.PlacePager;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlaceDto {
    private List<Place> Places;
    private PlacePager pager;

    @Builder
    public PlaceDto(List<Place> places, PlacePager pager) {
        Places = places;
        this.pager = pager;
    }
}
