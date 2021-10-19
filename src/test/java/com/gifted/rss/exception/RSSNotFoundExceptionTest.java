package com.gifted.rss.exception;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSSNotFoundExceptionTest {

    private Logger logger = LoggerFactory.getLogger(RSSNotFoundExceptionTest.class);
    private static String CONTENT_NOT_FOUND_MSG = "Old RSSFeed Not Found!";

    @Test
    public void rssNotFoundExceptionTest() {
        logger.info("RSS Not Found Exception Test");
        RSSFeedNotFound rssFeedNotFound = new RSSFeedNotFound(CONTENT_NOT_FOUND_MSG);
        Assert.assertNotNull(rssFeedNotFound);
        Assert.assertEquals(CONTENT_NOT_FOUND_MSG, rssFeedNotFound.getMessage());
    }

}