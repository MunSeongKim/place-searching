package com.mskim.place_searching.place.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Place {

    private String id;
    private String name;
    private String address;
    private String roadAddress;
    private String phone;
    private String shortcutLink;

    @Builder
    public Place(String id, String name, String address, String roadAddress, String phone, String shortcutLink) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.phone = phone;
        this.shortcutLink = shortcutLink;
    }
}
