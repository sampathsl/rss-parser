package com.gifted.rss.util;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.service.RSSFeedService;
import com.rometools.rome.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        List<RSSFeed> rssFeeds = new ArrayList<>();
        feed.getEntries().forEach(item -> {
            Timestamp updatedDate = item.getUpdatedDate() != null ? new Timestamp(item.getUpdatedDate().getTime()) :
                    new Timestamp(item.getPublishedDate().getTime());
            rssFeeds.add(new RSSFeed(item.getLink().trim(), item.getTitle().trim(), item.getDescription().getValue().trim(),
                    new Timestamp(item.getPublishedDate().getTime()), updatedDate));
        });

        Collections.sort(rssFeeds);
        List<RSSFeed> latestRssFeeds = rssFeeds.stream().limit(Constant.DEFAULT_MAX_DB_FETCH_COUNT).collect(Collectors.toList());
        List<RSSFeed> existingRssFeeds = rssFeedService.getLatestRSSFeeds(Constant.DEFAULT_PAGE, Constant.DEFAULT_MAX_DB_FETCH_COUNT,
                Constant.DEFAULT_SORT_BY, Constant.DEFAULT_DIRECTION).toList();
        logger.info(String.valueOf(latestRssFeeds.stream().count()));
        List<RSSFeed> newUpdatedRSSFeeds = getNewUpdatedRSSFeeds(latestRssFeeds, existingRssFeeds);
        logger.info(String.valueOf(newUpdatedRSSFeeds.stream().count()));
        rssFeedService.addRSSFeeds(newUpdatedRSSFeeds);
    }

    private List<RSSFeed> getNewUpdatedRSSFeeds(List<RSSFeed> latestRssFeeds, List<RSSFeed> existingRssFeeds) {
        return latestRssFeeds.stream()
                .filter(element -> !existingRssFeeds.contains(element))
                .collect(Collectors.toList());
    }

}
