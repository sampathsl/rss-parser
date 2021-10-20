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
    PUBLICATION_DATE("publicationDate"),
    UPDATED_DATE("updatedDate");

    private final String value;

    SORTBY(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
