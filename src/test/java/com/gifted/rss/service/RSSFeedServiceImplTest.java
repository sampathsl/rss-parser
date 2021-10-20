package com.gifted.rss.service;

import com.gifted.rss.dto.RSSFeedDto;
import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.repository.RSSFeedRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RSSFeedServiceImplTest {

    private Logger logger = LoggerFactory.getLogger(RSSFeedServiceImplTest.class);

    private RSSFeedService rssFeedService;

    @Mock
    private RSSFeedRepository rssFeedRepository;

    @Mock
    private Page<RSSFeed> rssFeedPage;

    @Before
    public void setup() {
        rssFeedService = new RSSFeedServiceImpl(rssFeedRepository);
        List<RSSFeed> mockRSSFeeds = this.getMockRSRssFeeds();
        Pageable paging = PageRequest.of(0, 2);
        rssFeedPage = new PageImpl<>(mockRSSFeeds.subList(0, 2), paging, mockRSSFeeds.size());
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

    @Test
    public void getLatestRSSDataTest() {
        logger.info("Get All RSSFeeds Data Test");
        Pageable pageable = PageRequest.of(0, 2, Sort.by("updatedDate").ascending());
        Mockito.when(rssFeedRepository.findAll(pageable)).thenReturn(rssFeedPage);
        Page<RSSFeedDto> pagableRSSFeeds = rssFeedService.getLatestRSSData(0, 2, "updatedDate", "asc");
        Assert.assertNotNull(pagableRSSFeeds);
        Assert.assertEquals(rssFeedPage.get().count(), pagableRSSFeeds.get().count());
    }

    @Test
    public void addRSSFeedsTest() {
        logger.info("Save RSSFeeds Test");
        Mockito.doReturn(this.getMockRSRssFeeds()).when(rssFeedRepository).saveAll(Mockito.any(List.class));
        List<RSSFeed> rssFeeds = rssFeedService.addRSSFeeds(this.getMockRSRssFeeds());
        Assert.assertNotNull(rssFeeds);
        Assert.assertEquals(1, rssFeeds.get(0).compareTo(this.getMockRSRssFeeds().get(0)));
        Assert.assertEquals(this.getMockRSRssFeeds().size(), rssFeeds.size());
    }

    @Test
    public void updateRSSFeed() {
        logger.info("Update RSSFeeds Test");
        RSSFeed mockRSSFeed = Mockito.mock(RSSFeed.class);
        Mockito.doReturn(mockRSSFeed).when(rssFeedRepository).save(Mockito.any(RSSFeed.class));
        RSSFeed rssFeeds = rssFeedService.updateRSSFeed(mockRSSFeed);
        Assert.assertNotNull(rssFeeds);
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

        RSSFeed rssFeedThree = new RSSFeed("test-link three", "test title three",
                "test description three", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        mockRSSFeeds.add(rssFeedTwo);
        mockRSSFeeds.add(rssFeedOne);
        mockRSSFeeds.add(rssFeedThree);
        return mockRSSFeeds;
    }

}
