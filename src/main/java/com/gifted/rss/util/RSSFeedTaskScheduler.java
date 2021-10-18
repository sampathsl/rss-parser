package com.gifted.rss.util;

import com.rometools.rome.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;

@Component
public class RSSFeedTaskScheduler {

    private Logger logger;

    @Value("${rss.feed}")
    private String rssFeed;

    public RSSFeedTaskScheduler() {
        this.logger = LoggerFactory.getLogger(RSSFeedTaskScheduler.class);
    }

    @Scheduled(cron = "${rss.feed.scheduler.expression:0 0/1 * * * ?}")
    public void scheduleFixedDelayTask() throws IOException, FeedException {
        // TODO
        this.logger.info("Fixed delay task - " + System.currentTimeMillis() / 1000);
        SyndFeedInput input = new SyndFeedInput();
        URL feedUrl = new URL(rssFeed);
        SyndFeed feed = input.build(new XmlReader(feedUrl));
        feed.getEntries().forEach(item -> {
            this.logger.info(item.getTitle());
            this.logger.info(item.getDescription().getValue());
            this.logger.info(item.getPublishedDate().toString());
            this.logger.info(item.getLink());
        });
    }

}
