package com.gifted.rss.util.enums;

public enum DIRECTION {
    ASC("asc"),
    DESC("desc");

    private final String value;

    DIRECTION(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
