package com.mskim.place_searching.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Place {

    private int id;
    private String name;
    private String address;
    @JsonProperty("road_address")
    private String roadAddress;
    private String phone;
    @JsonProperty("shortcut_link")
    private String shortcutLink;

    @Builder
    public Place(int id, String name, String address, String roadAddress, String phone, String shortcutLink) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.phone = phone;
        this.shortcutLink = shortcutLink;
    }
}
