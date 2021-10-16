package com.gifted.rss.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RSSFeedDto implements Serializable {

    @NotBlank
    private String link;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private Timestamp publicationDate;

    @NotBlank
    private Timestamp updatedDate;

}
