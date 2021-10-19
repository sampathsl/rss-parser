package com.gifted.rss.dto;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class RSSFeedDtoTest {

    @Test
    public void rssFeedDtoTest() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        RSSFeedDto rssFeedDto = new RSSFeedDto("test-link", "test-title", "test-description", now, now);
        Assert.assertEquals("test-link", rssFeedDto.getLink());
        Assert.assertEquals("test-title", rssFeedDto.getTitle());
        Assert.assertEquals("test-description", rssFeedDto.getDescription());
        Assert.assertEquals(now, rssFeedDto.getPublicationDate());
        Assert.assertEquals(now, rssFeedDto.getUpdatedDate());
    }

}
