package com.gifted.rss.service;

import com.gifted.rss.dto.RSSFeedDto;
import com.gifted.rss.entity.RSSFeed;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RSSFeedService {
    public RSSFeed addRSSFeed(RSSFeed rssFeed);
    public List<RSSFeed> addRSSFeeds(List<RSSFeed> rssFeeds);
    public Optional<RSSFeed> getRSSFeed(Long id);
    public Page<RSSFeed> getLatestRSSFeeds(Integer page, Integer size, String sortBy, String direction);
    public Page<RSSFeedDto> getLatestRSSData(Integer page, Integer size, String sortBy, String direction);
    public Optional<RSSFeed> updateRSSFeed(RSSFeed updateRSSFeed);
    public String deleteRSSFeedById(Long id);
}
