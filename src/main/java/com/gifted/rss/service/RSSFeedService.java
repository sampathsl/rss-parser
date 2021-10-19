package com.gifted.rss.service;

import com.gifted.rss.dto.RSSFeedDto;
import com.gifted.rss.entity.RSSFeed;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RSSFeedService {
    public List<RSSFeed> addRSSFeeds(List<RSSFeed> rssFeeds);
    public Page<RSSFeed> getLatestRSSFeeds(Integer page, Integer size, String sortBy, String direction);
    public Page<RSSFeedDto> getLatestRSSData(Integer page, Integer size, String sortBy, String direction);
    public RSSFeed updateRSSFeed(RSSFeed updateRSSFeed);
}
