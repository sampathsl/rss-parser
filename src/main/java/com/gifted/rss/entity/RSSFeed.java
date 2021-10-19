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
public class RSSFeed extends BaseEntity implements Comparable<RSSFeed> {

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

    @Override
    public int compareTo(RSSFeed rssFeed) {
        return this.getPublicationDate().compareTo((rssFeed).getPublicationDate()) == 1 ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RSSFeed rssFeed = (RSSFeed) o;

        if (link != null ? !link.equals(rssFeed.link) : rssFeed.link != null) return false;
        if (title != null ? !title.equals(rssFeed.title) : rssFeed.title != null) return false;
        if (description != null ? !description.equals(rssFeed.description) : rssFeed.description != null) return false;
        return publicationDate != null ? publicationDate.equals(rssFeed.publicationDate) : rssFeed.publicationDate == null;
    }

    @Override
    public int hashCode() {
        int result = link != null ? link.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        return result;
    }
}
