package com.gifted.rss.util;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.exception.ConnectionErrorException;
import com.gifted.rss.service.RSSFeedService;
import com.rometools.rome.io.FeedException;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    private static Timestamp PUBLICATIONDATETWO = new Timestamp(System.currentTimeMillis() - 8000);
    private static Timestamp UPDATEDDATETWO = new Timestamp(System.currentTimeMillis() - 7000);
    private static Timestamp PUBLICATIONDATETHREE = new Timestamp(System.currentTimeMillis() - 6000);
    private static Timestamp UPDATEDDATEFOUR = new Timestamp(System.currentTimeMillis() - 5000);
    private static Timestamp PUBLICATIONDATEFOUR = new Timestamp(System.currentTimeMillis() - 4000);
    private static Timestamp PUBLICATIONDATEFIVE = new Timestamp(System.currentTimeMillis() - 3000);
    private static Timestamp PUBLICATIONDATESIX = new Timestamp(System.currentTimeMillis() - 2000);

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
        Assert.assertEquals(3, updateRSSFeedCount);
    }

    @Test
    public void addAllRSSFeedsTest() {
        List<RSSFeed> list = Mockito.mock(List.class);
        RSSFeedTaskScheduler rssFeedTaskSchedulerSpy = Mockito.spy(rssFeedTaskScheduler);
        rssFeedTaskSchedulerSpy.updateAllRSSFeeds(list);
        Mockito.verify(rssFeedTaskSchedulerSpy).updateAllRSSFeeds(list);
    }

    @Test
    public void addRSSFeedsTest() {
        logger.info("Add RSSFeeds Test");
        List<RSSFeed> list = Mockito.mock(List.class);
        RSSFeedTaskScheduler rssFeedTaskSchedulerSpy = Mockito.spy(rssFeedTaskScheduler);
        rssFeedTaskSchedulerSpy.addAllRSSFeeds(list);
        Mockito.verify(rssFeedTaskSchedulerSpy).addAllRSSFeeds(list);
    }

    // TODO
//    @Test
//    public void loadRSSFeedsTest() throws FeedException, ConnectionErrorException, IOException {
//    }

    private List<RSSFeed> getMockExistingRSSFeeds() {
        List<RSSFeed> mockRSSFeeds = new ArrayList<>();
        RSSFeed rssFeedOne = new RSSFeed();
        rssFeedOne.setId(1l);
        rssFeedOne.setLink("test-link");
        rssFeedOne.setTitle("test title one");
        rssFeedOne.setDescription("test description one");
        rssFeedOne.setPublicationDate(PUBLICATIONDATEONE);
        rssFeedOne.setUpdatedDate(PUBLICATIONDATEONE);

        RSSFeed rssFeedTwo = new RSSFeed();
        rssFeedTwo.setId(2l);
        rssFeedTwo.setLink("test-link");
        rssFeedTwo.setTitle("test title two");
        rssFeedTwo.setDescription("test description two");
        rssFeedTwo.setPublicationDate(PUBLICATIONDATETWO);
        rssFeedTwo.setUpdatedDate(PUBLICATIONDATETWO);

        RSSFeed rssFeedThree = new RSSFeed();
        rssFeedThree.setId(3l);
        rssFeedThree.setLink("test-link");
        rssFeedThree.setTitle("test title three");
        rssFeedThree.setDescription("test description three");
        rssFeedThree.setPublicationDate(PUBLICATIONDATETHREE);
        rssFeedThree.setUpdatedDate(PUBLICATIONDATETHREE);

        RSSFeed rssFeedFour = new RSSFeed();
        rssFeedFour.setId(4l);
        rssFeedFour.setLink("test-link");
        rssFeedFour.setTitle("test title four");
        rssFeedFour.setDescription("test description four");
        rssFeedFour.setPublicationDate(PUBLICATIONDATEFOUR);
        rssFeedFour.setUpdatedDate(PUBLICATIONDATEFOUR);

        RSSFeed rssFeedFive = new RSSFeed();
        rssFeedFive.setId(5l);
        rssFeedFive.setLink("test-link");
        rssFeedFive.setTitle("test title five");
        rssFeedFive.setDescription("test description five");
        rssFeedFive.setPublicationDate(PUBLICATIONDATEFIVE);
        rssFeedFive.setUpdatedDate(PUBLICATIONDATEFIVE);

        mockRSSFeeds.add(rssFeedTwo);
        mockRSSFeeds.add(rssFeedOne);
        mockRSSFeeds.add(rssFeedThree);
        mockRSSFeeds.add(rssFeedFour);
        mockRSSFeeds.add(rssFeedFive);
        return mockRSSFeeds;
    }

    private List<RSSFeed> getMockRequestedRSSFeeds() {
        List<RSSFeed> mockRSSFeeds = new ArrayList<>();
        RSSFeed rssFeedOne = new RSSFeed();
        rssFeedOne.setLink("test-link");
        rssFeedOne.setTitle("test title one");
        rssFeedOne.setDescription("test description one");
        rssFeedOne.setPublicationDate(PUBLICATIONDATEONE);
        rssFeedOne.setUpdatedDate(null);

        RSSFeed rssFeedTwo = new RSSFeed();
        rssFeedTwo.setLink("test-link");
        rssFeedTwo.setTitle("test title two");
        rssFeedTwo.setDescription("test description two updated");
        rssFeedTwo.setPublicationDate(PUBLICATIONDATETWO);
        rssFeedTwo.setUpdatedDate(UPDATEDDATETWO);

        RSSFeed rssFeedThree = new RSSFeed();
        rssFeedThree.setLink("test-link");
        rssFeedThree.setTitle("test title three");
        rssFeedThree.setDescription("test description three");
        rssFeedThree.setPublicationDate(PUBLICATIONDATETHREE);
        rssFeedThree.setUpdatedDate(null);

        RSSFeed rssFeedFour = new RSSFeed();
        rssFeedFour.setLink("test-link");
        rssFeedFour.setTitle("test title four updated");
        rssFeedFour.setDescription("test description four");
        rssFeedFour.setPublicationDate(PUBLICATIONDATEFOUR);
        rssFeedFour.setUpdatedDate(UPDATEDDATEFOUR);

        RSSFeed rssFeedFive = new RSSFeed();
        rssFeedFive.setLink("test-link five updated");
        rssFeedFive.setTitle("test title five");
        rssFeedFive.setDescription("test description five");
        rssFeedFive.setPublicationDate(PUBLICATIONDATEFIVE);
        rssFeedFive.setUpdatedDate(null);

        RSSFeed rssFeedSix = new RSSFeed();
        rssFeedSix.setLink("test-link");
        rssFeedSix.setTitle("test title six");
        rssFeedSix.setDescription("test description six");
        rssFeedSix.setPublicationDate(PUBLICATIONDATESIX);
        rssFeedSix.setUpdatedDate(null);

        mockRSSFeeds.add(rssFeedThree);
        mockRSSFeeds.add(rssFeedTwo);
        mockRSSFeeds.add(rssFeedOne);
        mockRSSFeeds.add(rssFeedFour);
        mockRSSFeeds.add(rssFeedFive);
        mockRSSFeeds.add(rssFeedSix);
        return mockRSSFeeds;
    }

}
