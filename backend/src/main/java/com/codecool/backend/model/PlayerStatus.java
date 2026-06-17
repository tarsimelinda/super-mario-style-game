package com.codecool.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PlayerStatus {
    PLAYING("playing"),
    MENU("menu"),
    DEAD("dead");

    private final String value;

    PlayerStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PlayerStatus fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (PlayerStatus status : values()) {
            if (status.value.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid player status: " + value);
    }
}
