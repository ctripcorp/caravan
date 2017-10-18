package com.ctrip.soa.caravan.util.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by w.jian on 2017/2/28.
 */
public enum Color {

    RED("Red"),

    GREEN("Green"),

    BLUE("Blue");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static Color fromValue(String v) {
        for (Color c: Color.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
