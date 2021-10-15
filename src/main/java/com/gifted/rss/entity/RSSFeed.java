package com.gifted.rss.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rss_feed")
public class RSSFeed extends BaseEntity {

    @NotBlank
    private String link;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Column(name = "publication_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp publicationDate;

    @Column(name = "updated_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp updatedDate;

}
