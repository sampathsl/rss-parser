package com.gifted.rss.util;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.exception.RSSFeedNotFound;
import com.gifted.rss.service.RSSFeedService;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rometools.rome.io.SyndFeedInput;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RSSFeedTaskScheduler {

    @Value("${rss.feed}")
    private String rssFeed;

    private Logger logger = LoggerFactory.getLogger(RSSFeedTaskScheduler.class);
    private RSSFeedService rssFeedService;
    private ZonedDateTime zdt = ZonedDateTime.parse("Sat, 01 Jan 2000 00:00:00 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);

    public RSSFeedTaskScheduler() {
        this.logger = LoggerFactory.getLogger(RSSFeedTaskScheduler.class);
    }

    @Autowired
    public RSSFeedTaskScheduler(RSSFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    public RSSFeedTaskScheduler(Logger logger, RSSFeedService rssFeedService, String rssFeed, ZonedDateTime zdt) {
        this.logger = logger;
        this.rssFeedService = rssFeedService;
        this.rssFeed = rssFeed;
        this.zdt = zdt;
    }

    @Scheduled(cron = "${rss.feed.scheduler.expression:0 0/5 * * * ?}")
    public void scheduleFixedDelayTask() throws IOException, FeedException {
        this.logger.info("Fixed delay task - " + System.currentTimeMillis() / 1000);
        SyndFeedInput input = new SyndFeedInput();
        URL feedUrl = new URL(rssFeed);
        URLConnection conn = feedUrl.openConnection();
        Map<String, List<String>> map = conn.getHeaderFields();
        ZonedDateTime zdtLatest = ZonedDateTime.parse(map.get("Last-Modified").get(0), DateTimeFormatter.RFC_1123_DATE_TIME);

        if ((zdt == null) || ((zdt != null) && zdt.isBefore(zdtLatest))) {
            this.zdt = zdtLatest;
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<RSSFeed> rssFeeds = new ArrayList<>();
            List<RSSFeed> updatedRSSFeeds = new ArrayList<>();
            feed.getEntries().forEach(item -> {
                Timestamp updatedDate = item.getUpdatedDate() != null ? new Timestamp(item.getUpdatedDate().getTime()) :
                        new Timestamp(item.getPublishedDate().getTime());
                RSSFeed rssFeed = new RSSFeed(item.getLink().trim(), item.getTitle().trim(), item.getDescription().getValue().trim(),
                        new Timestamp(item.getPublishedDate().getTime()), updatedDate);
                rssFeeds.add(rssFeed);
                if (item.getUpdatedDate() != null) {
                    updatedRSSFeeds.add(rssFeed);
                }
            });

            Collections.sort(rssFeeds);
            List<RSSFeed> latestRssFeeds = rssFeeds.stream().limit(Constant.DEFAULT_MAX_DB_FETCH_COUNT).collect(Collectors.toList());
            List<RSSFeed> existingRssFeeds = rssFeedService.getLatestRSSFeeds(Constant.DEFAULT_PAGE, Constant.DEFAULT_MAX_DB_FETCH_COUNT,
                    Constant.DEFAULT_SORT_BY, Constant.DEFAULT_DIRECTION).toList();
            List[] rssFeedsLists = getNewAndUpdatedRSSFeeds(existingRssFeeds, latestRssFeeds);
            logger.info(String.valueOf(rssFeedsLists[0].stream().count()));
            logger.info(String.valueOf(rssFeedsLists[1].stream().count()));
            addAllRSSFeeds(rssFeedsLists[0]);
            updateAllRSSFeeds(rssFeedsLists[1]);
        }

    }

    private void addAllRSSFeeds(List<RSSFeed> newRssFeeds) {
        rssFeedService.addRSSFeeds(newRssFeeds);
    }

    private void updateAllRSSFeeds(List<RSSFeed> updatedRssFeeds) {
        updatedRssFeeds.forEach(rssFedd -> {
            rssFeedService.updateRSSFeed(rssFedd);
        });
    }

    private List[] getNewAndUpdatedRSSFeeds(List<RSSFeed> existingRssFeeds, List<RSSFeed> latestRssFeeds) {
        List[] newAndUpdatedRSSFeeds = new List[2];
        List<RSSFeed> newRSSFeeds = new ArrayList<>();
        List<RSSFeed> updatedRSSFeeds = new ArrayList<>();
        List<RSSFeed> allNewAndUpdatedRSSFeeds = getAllNewAndUpdatedRSSFeeds(existingRssFeeds, latestRssFeeds);
        allNewAndUpdatedRSSFeeds.forEach(rssFeed -> {
            try {
                RSSFeed updatedRSSFeed = updateExistingRSSFeed(existingRssFeeds, rssFeed);
                if (updatedRSSFeed.isNew()) {
                    newRSSFeeds.add(updatedRSSFeed);
                } else {
                    updatedRSSFeeds.add(updatedRSSFeed);
                }
            } catch (RSSFeedNotFound exception) {
                logger.error("Existing RSSFeed Not Found for ", rssFeed.getTitle(), exception.getMessage());
            }
        });
        newAndUpdatedRSSFeeds[0] = newRSSFeeds;
        newAndUpdatedRSSFeeds[1] = updatedRSSFeeds;
        return newAndUpdatedRSSFeeds;
    }

    private List<RSSFeed> getAllNewAndUpdatedRSSFeeds(List<RSSFeed> existingRssFeeds, List<RSSFeed> latestRssFeeds) {
        return latestRssFeeds.stream().filter(rssFeed -> !existingRssFeeds.contains(rssFeed)).distinct().collect(Collectors.toList());
    }

    private RSSFeed updateExistingRSSFeed(List<RSSFeed> oldRssFeeds, RSSFeed updatedRSSFeed) throws RSSFeedNotFound {
        List<RSSFeed> newOrUpdatedList = new ArrayList<>();
        oldRssFeeds.stream()
                .filter(old -> (updatedRSSFeed.getLink().equals(old.getLink()) &&
                        updatedRSSFeed.getTitle().equals(old.getTitle())) ||
                        (updatedRSSFeed.getLink().equals(old.getLink()) &&
                                updatedRSSFeed.getDescription().equals(old.getDescription())) ||
                        (updatedRSSFeed.getTitle().equals(old.getLink()) &&
                                updatedRSSFeed.getDescription().equals(old.getDescription())))
                .forEach(newOrUpdatedList::add);
        if (oldRssFeeds.size() == 0) {
            return updatedRSSFeed;
        } else if (oldRssFeeds.size() == 1) {
            return updateOldRSSFeed(oldRssFeeds.get(0), updatedRSSFeed);
        }
        throw new RSSFeedNotFound("Old RSSFeed Feed Not Found!");
    }

    private RSSFeed updateOldRSSFeed(RSSFeed oldRSSFeed, RSSFeed newRSSFeed) {
        oldRSSFeed.setLink(newRSSFeed.getLink());
        oldRSSFeed.setTitle(newRSSFeed.getTitle());
        oldRSSFeed.setDescription(newRSSFeed.getDescription());
        oldRSSFeed.setPublicationDate(newRSSFeed.getPublicationDate());
        oldRSSFeed.setUpdatedDate(newRSSFeed.getUpdatedDate());
        return oldRSSFeed;
    }

}
