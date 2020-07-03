package com.mskim.place_searching.app.place.support.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class DisplayPage {
    private boolean isActive = false;
    private int value;
}
