package com.mskim.place_searching.place.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Place {
    private String name;

    @Builder
    public Place(String name) {
        this.name = name;
    }
}
