package com.gifted.rss.util;

import com.gifted.rss.dto.RSSFeedDto;
import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.service.RSSFeedService;
import com.rometools.rome.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class RSSFeedTaskScheduler {

    private Logger logger = LoggerFactory.getLogger(RSSFeedTaskScheduler.class);

    private RSSFeedService rssFeedService;

    @Value("${rss.feed}")
    private String rssFeed;

    public RSSFeedTaskScheduler() {
        this.logger = LoggerFactory.getLogger(RSSFeedTaskScheduler.class);
    }

    @Autowired
    public RSSFeedTaskScheduler(RSSFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    public RSSFeedTaskScheduler(Logger logger, RSSFeedService rssFeedService, String rssFeed) {
        this.logger = logger;
        this.rssFeedService = rssFeedService;
        this.rssFeed = rssFeed;
    }

    @Scheduled(cron = "${rss.feed.scheduler.expression:0 0/5 * * * ?}")
    public void scheduleFixedDelayTask() throws IOException, FeedException {
        this.logger.info("Fixed delay task - " + System.currentTimeMillis() / 1000);
        SyndFeedInput input = new SyndFeedInput();
        URL feedUrl = new URL(rssFeed);
        SyndFeed feed = input.build(new XmlReader(feedUrl));
        List<RSSFeedDto> rssFeeds = new ArrayList<>();
        feed.getEntries().forEach(item -> {
            // READ RSS
            // READ FROM DB
            // COMPARE
            // NEW ITEM INSERT
            // UPDATE
            Timestamp updatedDate = item.getUpdatedDate() != null ? new Timestamp(item.getUpdatedDate().getTime()) :
            new Timestamp(item.getPublishedDate().getTime());

            rssFeeds.add(new RSSFeedDto(item.getLink(),item.getTitle(),item.getDescription().getValue(),
                    new Timestamp(item.getPublishedDate().getTime()),updatedDate));
        });

        Page<RSSFeed> existingRssFeeds = rssFeedService.getLatestRSSFeeds(Constant.DEFAULT_PAGE, 100,
                Constant.DEFAULT_SORT_BY, Constant.DEFAULT_DIRECTION);
        logger.info(String.valueOf(rssFeeds.stream().count()));
        logger.info(String.valueOf(existingRssFeeds.getTotalElements()));


    }

}
