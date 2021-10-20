package com.gifted.rss.util;

import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.exception.ConnectionErrorException;
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

    /**
     * Scheduler process for load RSSFeed data from the RSSFeed source
     * @throws IOException throws {@link IOException} when the RSSFeed source connection is not established
     * @throws FeedException throws {@link FeedException} when the malformed RSSFeed feed received
     * @throws ConnectionErrorException throws {@link ConnectionErrorException} when the RSSFeed source connection is not established
     */
    @Scheduled(cron = "${rss.feed.scheduler.expression:0 0/5 * * * ?}")
    public void loadRSSFeeds() throws IOException, FeedException, ConnectionErrorException {

        this.logger.info("Start executing RSSFeed Loader");
        SyndFeedInput input = new SyndFeedInput();
        URL feedUrl = new URL(rssFeed);
        URLConnection conn = feedUrl.openConnection();
        Map<String, List<String>> map = conn.getHeaderFields();
        if (map.size() == 0) {
            throw new ConnectionErrorException("Could not able to connect RSS Feed server!");
        }

        ZonedDateTime zdtLatest = ZonedDateTime.parse(map.get("Last-Modified").get(0), DateTimeFormatter.RFC_1123_DATE_TIME);
        if ((zdt == null) || (zdt.isBefore(zdtLatest))) {
            this.logger.info("Getting new RSS Feeds {}", zdtLatest);
            this.zdt = zdtLatest;
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<RSSFeed> rssFeeds = new ArrayList<>();
            List<RSSFeed> updatedRSSFeeds = new ArrayList<>();
            feed.getEntries().forEach(item -> {
                Timestamp updatedDate = item.getUpdatedDate() != null ? new Timestamp(item.getUpdatedDate().getTime()) :
                        new Timestamp(item.getPublishedDate().getTime());
                RSSFeed newRssFeed = new RSSFeed(item.getLink().trim(), item.getTitle().trim(), item.getDescription().getValue().trim(),
                        new Timestamp(item.getPublishedDate().getTime()), updatedDate);
                rssFeeds.add(newRssFeed);
                if (item.getUpdatedDate() != null) {
                    updatedRSSFeeds.add(newRssFeed);
                }
            });

            Collections.sort(rssFeeds);
            List<RSSFeed> latestRssFeeds = rssFeeds.stream().limit(Constant.DEFAULT_MAX_DB_FETCH_COUNT).collect(Collectors.toList());
            List<RSSFeed> existingRssFeeds = rssFeedService.getLatestRSSFeeds(Constant.DEFAULT_PAGE, Constant.DEFAULT_MAX_DB_FETCH_COUNT,
                    Constant.DEFAULT_SORT_BY, Constant.DEFAULT_DIRECTION).toList();

            // Update records with already captured
            List[] rssFeedsLists = getNewAndUpdatedRSSFeeds(existingRssFeeds, updatedRSSFeeds);
            String newRSSFeedCount = String.valueOf(rssFeedsLists[0].stream().count());
            String updatedRSSFeedCount = String.valueOf(rssFeedsLists[1].stream().count());
            logger.info("New RSS Feed Count - by getUpdatedDate: {}", newRSSFeedCount);
            logger.info("Updated RSS Feed Count - by getUpdatedDate: {}", updatedRSSFeedCount);
            updateAllRSSFeeds(rssFeedsLists[1]);

            // Remove already updated RSSFeeds
            latestRssFeeds.removeAll(updatedRSSFeeds);
            rssFeedsLists = getNewAndUpdatedRSSFeeds(existingRssFeeds, latestRssFeeds);
            newRSSFeedCount = String.valueOf(rssFeedsLists[0].stream().count());
            updatedRSSFeedCount = String.valueOf(rssFeedsLists[1].stream().count());
            logger.info("New RSS Feed Count: {}", newRSSFeedCount);
            logger.info("Updated RSS Feed Count: {}", updatedRSSFeedCount);
            addAllRSSFeeds(rssFeedsLists[0]);
            updateAllRSSFeeds(rssFeedsLists[1]);
        } else {
            this.logger.info("New RSS Feeds Not Found. Ending  Scheduler Process {}", zdt);
        }

    }

    /**
     * Add all the RSSFeed for given list of {@link RSSFeed}
     * @param newRssFeeds the list of new {@link RSSFeed}
     */
    public void addAllRSSFeeds(List<RSSFeed> newRssFeeds) {
        rssFeedService.addRSSFeeds(newRssFeeds);
    }

    /**
     * Update all the modified RSSFeeds for given list of {@link RSSFeed}
     * @param updatedRssFeeds the list of updated {@link RSSFeed}
     */
    public void updateAllRSSFeeds(List<RSSFeed> updatedRssFeeds) {
        updatedRssFeeds.forEach(rssFeedForUpdate -> rssFeedService.updateRSSFeed(rssFeedForUpdate));
    }

    /**
     * Extract updated list of {@link RSSFeed} and new list of {@link RSSFeed} from the given list of
     * existing RSSFeeds and incoming RSSFeeds lists
     * @param existingRssFeeds the list of existing RSSFeeds from database
     * @param latestRssFeeds the list of incoming RSSFeeds from RSSFeed source
     * @return the array of {@link List} containing new {@link RSSFeed} and updated {@link RSSFeed}
     */
    public List[] getNewAndUpdatedRSSFeeds(List<RSSFeed> existingRssFeeds, List<RSSFeed> latestRssFeeds) {
        List[] newAndUpdatedRSSFeeds = new List[2];
        List<RSSFeed> newRSSFeeds = new ArrayList<>();
        List<RSSFeed> updatedRSSFeeds = new ArrayList<>();
        List<RSSFeed> allNewAndUpdatedRSSFeeds = getAllNewAndUpdatedRSSFeeds(existingRssFeeds, latestRssFeeds);
        allNewAndUpdatedRSSFeeds.forEach(rf -> {
            RSSFeed newOrUpdatedRSSFeed = updateExistingRSSFeed(existingRssFeeds, rf);
            if (newOrUpdatedRSSFeed.isNew()) {
                newRSSFeeds.add(newOrUpdatedRSSFeed);
            } else {
                updatedRSSFeeds.add(newOrUpdatedRSSFeed);
            }
        });
        newAndUpdatedRSSFeeds[0] = newRSSFeeds;
        newAndUpdatedRSSFeeds[1] = updatedRSSFeeds;
        return newAndUpdatedRSSFeeds;
    }

    /**
     * Remove unmodified RSSFeeds from the incoming RSSFeeds
     * @param existingRssFeeds the list of existing RSSFeeds from database
     * @param latestRssFeeds the list of incoming RSSFeeds from RSSFeed source
     * @return the list of new and modified RSSFeeds
     */
    private List<RSSFeed> getAllNewAndUpdatedRSSFeeds(List<RSSFeed> existingRssFeeds, List<RSSFeed> latestRssFeeds) {
        return latestRssFeeds.stream().filter(rf -> !existingRssFeeds.contains(rf)).distinct().collect(Collectors.toList());
    }

    /**
     * Update the modified RSSFeed from the extracted existing RSSFeed
     * If there are any matching existing RSSFeed not found then the given RSSFeed considered as new RSSFeed
     * @param oldRssFeeds the list of existing RSSFeeds from database
     * @param updatedRSSFeed the incoming RSSFeed from the source
     * @return the new or modified {@link RSSFeed}
     */
    private RSSFeed updateExistingRSSFeed(List<RSSFeed> oldRssFeeds, RSSFeed updatedRSSFeed) {
        List<RSSFeed> newOrUpdatedList = new ArrayList<>();
        oldRssFeeds.stream()
                .filter(old -> (updatedRSSFeed.getLink().equals(old.getLink()) &&
                        updatedRSSFeed.getTitle().equals(old.getTitle())) ||
                        (updatedRSSFeed.getLink().equals(old.getLink()) &&
                                updatedRSSFeed.getDescription().equals(old.getDescription())) ||
                        (updatedRSSFeed.getTitle().equals(old.getTitle()) &&
                                updatedRSSFeed.getDescription().equals(old.getDescription())))
                .forEach(newOrUpdatedList::add);
        if (newOrUpdatedList.size() == 1) {
            return updateOldRSSFeed(oldRssFeeds.get(0), updatedRSSFeed);
        }
        return updatedRSSFeed;
    }

    /**
     * Update the existing db RSSFeed information with incoming updated RSSFeed
     * @param oldRSSFeed the existing {@link RSSFeed} object
     * @param newRSSFeed the incoming modified {@link RSSFeed} object
     * @return the modified {@link RSSFeed} object with new data
     */
    private RSSFeed updateOldRSSFeed(RSSFeed oldRSSFeed, RSSFeed newRSSFeed) {
        oldRSSFeed.setLink(newRSSFeed.getLink());
        oldRSSFeed.setTitle(newRSSFeed.getTitle());
        oldRSSFeed.setDescription(newRSSFeed.getDescription());
        oldRSSFeed.setPublicationDate(newRSSFeed.getPublicationDate());
        oldRSSFeed.setUpdatedDate(newRSSFeed.getUpdatedDate());
        return oldRSSFeed;
    }

}
