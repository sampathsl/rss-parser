package com.gifted.rss.util;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.exception.RSSFeedNotFound;
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
import java.util.*;
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

        // TODO - Check RSS Feed Already updated or not
//        response = HTTParty.get(url)
//        headers = response.headers
//        last_modified = headers['Last-Modified']
//        etag = headers['Etag']
//        body = response.body
//        feed = Feedjira.parse(body) if(stored_last_modified != last_modified or etag != stored_etag)


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
