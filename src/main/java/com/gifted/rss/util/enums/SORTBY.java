/**
 * @author  Sampath Thennakoon
 * @version 1.0
 * @since   2021-10-20
 */

package com.gifted.rss.util.enums;

public enum SORTBY {
    ID("id"),
    LINK("link"),
    TITLE("title"),
    DESCRIPTION("description"),
    PUBLICATION_DATE("publication_date"),
    UPDATED_DATE("updated_date");

    private final String value;

    SORTBY(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
