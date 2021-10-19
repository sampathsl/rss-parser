package com.gifted.rss.util;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.service.RSSFeedService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RSSFeedTaskSchedulerTest {

    private Logger logger = LoggerFactory.getLogger(RSSFeedTaskSchedulerTest.class);
    private static String RSS_FEED_URL = "https://feeds.simplecast.com/54nAGcIl";
    private ZonedDateTime zdt = ZonedDateTime.parse("Sat, 01 Jan 2000 00:00:00 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);
    private static Timestamp PUBLICATIONDATEONE = new Timestamp(System.currentTimeMillis() - 10000);
    private static Timestamp PUBLICATIONDATETWO = new Timestamp(System.currentTimeMillis() - 5000);
    private static Timestamp UPDATEDDATETWO = new Timestamp(System.currentTimeMillis() - 1000);
    private static Timestamp PUBLICATIONDATETHREE = new Timestamp(System.currentTimeMillis());

    @Mock
    private RSSFeedService rssFeedService;
    private RSSFeedTaskScheduler rssFeedTaskScheduler;

    @Before
    public void setup() {
        rssFeedTaskScheduler = new RSSFeedTaskScheduler(
                logger, rssFeedService, RSS_FEED_URL, zdt
        );
    }

    @Test
    public void getAllNewAndUpdatedRSSFeedsTest() {
        List[] lists = rssFeedTaskScheduler.getNewAndUpdatedRSSFeeds(getMockExistingRSSFeeds(), getMockRequestedRSSFeeds());
        List<RSSFeed> newRSSFeed = lists[0];
        List<RSSFeed> updateRSSFeed = lists[1];
        int newRSSFeedCount = newRSSFeed.size();
        int updateRSSFeedCount = updateRSSFeed.size();
        Assert.assertEquals(1, newRSSFeedCount);
        Assert.assertEquals(1, updateRSSFeedCount);
    }

    private List<RSSFeed> getMockExistingRSSFeeds() {
        List<RSSFeed> mockRSSFeeds = new ArrayList<>();
        RSSFeed rssFeedOne = new RSSFeed();
        rssFeedOne.setId(1l);
        rssFeedOne.setLink("test-link one");
        rssFeedOne.setTitle("test title one");
        rssFeedOne.setDescription("test description one");
        rssFeedOne.setPublicationDate(PUBLICATIONDATEONE);
        rssFeedOne.setUpdatedDate(PUBLICATIONDATEONE);

        RSSFeed rssFeedTwo = new RSSFeed();
        rssFeedTwo.setId(2l);
        rssFeedTwo.setLink("test-link two");
        rssFeedTwo.setTitle("test title two");
        rssFeedTwo.setDescription("test description two");
        rssFeedTwo.setPublicationDate(PUBLICATIONDATETWO);
        rssFeedTwo.setUpdatedDate(PUBLICATIONDATETWO);

        mockRSSFeeds.add(rssFeedTwo);
        mockRSSFeeds.add(rssFeedOne);
        return mockRSSFeeds;
    }

    private List<RSSFeed> getMockRequestedRSSFeeds() {
        List<RSSFeed> mockRSSFeeds = new ArrayList<>();
        RSSFeed rssFeedOne = new RSSFeed();
        rssFeedOne.setLink("test-link one");
        rssFeedOne.setTitle("test title one");
        rssFeedOne.setDescription("test description one");
        rssFeedOne.setPublicationDate(PUBLICATIONDATEONE);
        rssFeedOne.setUpdatedDate(null);

        RSSFeed rssFeedTwo = new RSSFeed();
        rssFeedTwo.setLink("test-link two");
        rssFeedTwo.setTitle("test title two");
        rssFeedTwo.setDescription("test description two updated");
        rssFeedTwo.setPublicationDate(PUBLICATIONDATETWO);
        rssFeedTwo.setUpdatedDate(UPDATEDDATETWO);

        RSSFeed rssFeedThree = new RSSFeed();
        rssFeedThree.setLink("test-link three");
        rssFeedThree.setTitle("test title three");
        rssFeedThree.setDescription("test description three");
        rssFeedThree.setPublicationDate(PUBLICATIONDATETHREE);
        rssFeedThree.setUpdatedDate(null);

        mockRSSFeeds.add(rssFeedThree);
        mockRSSFeeds.add(rssFeedTwo);
        mockRSSFeeds.add(rssFeedOne);
        return mockRSSFeeds;
    }

}
