package com.mskim.place_searching.configuration.security;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER");

    private String value;

    Role(String role) {
        this.value = role;
    }
}
