package com.gifted.rss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
