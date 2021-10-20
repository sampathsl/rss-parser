/**
 * @author  Sampath Thennakoon
 * @version 1.0
 * @since   2021-10-20
 */

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
