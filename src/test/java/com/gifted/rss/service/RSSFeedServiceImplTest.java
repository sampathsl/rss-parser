package com.gifted.rss.service;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.repository.RSSFeedRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RSSFeedServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(RSSFeedServiceImplTest.class);

    private RSSFeedService rssFeedService;

    @Mock
    private RSSFeedRepository rssFeedRepository;

    @Mock
    private Page<RSSFeed> rssFeedPage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        rssFeedService = new RSSFeedServiceImpl(rssFeedRepository);
        List<RSSFeed> mockRSSFeeds = this.getMockRSRssFeeds();
        Pageable paging = PageRequest.of(0, 2);
        rssFeedPage = new PageImpl<RSSFeed>(mockRSSFeeds.subList(0, 2), paging, mockRSSFeeds.size());
    }

    @Test
    public void getLatestRSSFeedsTest() {
        logger.info("Get All RSSFeeds Test");
        Pageable pageable = PageRequest.of(0, 2, Sort.by("updatedDate").descending());
        Mockito.when(rssFeedRepository.findAll(pageable)).thenReturn(rssFeedPage);
        Page<RSSFeed> pagableRSSFeeds = rssFeedService.getLatestRSSFeeds(0, 2, "updatedDate", "desc");
        Assert.assertNotNull(pagableRSSFeeds);
        Assert.assertEquals(rssFeedPage.get().count(), pagableRSSFeeds.get().count());
    }

    private List<RSSFeed> getMockRSRssFeeds() {
        List<RSSFeed> mockRSSFeeds = new ArrayList<>();
        RSSFeed rssFeedOne = new RSSFeed();
        rssFeedOne.setLink("test-link one");
        rssFeedOne.setTitle("test title one");
        rssFeedOne.setDescription("test description one");
        rssFeedOne.setPublicationDate(new Timestamp(System.currentTimeMillis()));
        rssFeedOne.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        RSSFeed rssFeedTwo = new RSSFeed();
        rssFeedTwo.setLink("test-link two");
        rssFeedTwo.setTitle("test title two");
        rssFeedTwo.setDescription("test description two");
        rssFeedTwo.setPublicationDate(new Timestamp(System.currentTimeMillis()));
        rssFeedTwo.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        mockRSSFeeds.add(rssFeedTwo);
        mockRSSFeeds.add(rssFeedOne);
        return mockRSSFeeds;
    }

}
