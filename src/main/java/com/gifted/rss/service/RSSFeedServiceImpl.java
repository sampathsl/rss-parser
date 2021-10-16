package com.gifted.rss.service;

import com.gifted.rss.dto.RSSFeedDto;
import com.gifted.rss.entity.RSSFeed;
import com.gifted.rss.repository.RSSFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RSSFeedServiceImpl implements RSSFeedService {

    @Autowired
    private RSSFeedRepository rssFeedRepository;

    public RSSFeed addRSSFeed(RSSFeed rssFeed) {
        return rssFeedRepository.save(rssFeed);
    }

    public List<RSSFeed> addRSSFeeds(List<RSSFeed> rssFeeds) {
        return (List<RSSFeed>) rssFeedRepository.saveAll(rssFeeds);
    }

    public Optional<RSSFeed> getRSSFeed(Long id) {
        return rssFeedRepository.findById(id);
    }

    public Page<RSSFeedDto> getLatestRSSFeeds(Integer page, Integer size, String sortBy, String direction) {
        // TODO - PREPARE Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return toPageObjectDto(rssFeedRepository.findAll(pageable));
    }

    public Page<RSSFeedDto> toPageObjectDto(Page<RSSFeed> rssFeeds) {
        return rssFeeds.map(this::convertToObjectDto);
    }

    private RSSFeedDto convertToObjectDto(RSSFeed rssFeed) {
        RSSFeedDto dto = new RSSFeedDto();
        dto.setLink(rssFeed.getLink());
        dto.setTitle(rssFeed.getTitle());
        dto.setDescription(rssFeed.getDescription());
        dto.setPublicationDate(rssFeed.getPublicationDate());
        dto.setUpdatedDate(rssFeed.getUpdatedDate());
        return dto;
    }

    public Optional<RSSFeed> updateRSSFeed(RSSFeed updateRSSFeed) {
        // TODO - find unique???
        /*Optional<RSSFeed> optionalRSSFeedOld = rssFeedRepository.findById(updateRSSFeed.getId());
        if (optionalRSSFeedOld.isPresent()) {
            RSSFeed oldRSSFeed = optionalRSSFeedOld.get();
            oldRSSFeed.setDescription(oldRSSFeed.getDescription());
            oldRSSFeed.setTitle(oldRSSFeed.getTitle());
            oldRSSFeed.setUpdatedDate(oldRSSFeed.getUpdatedDate());
            oldRSSFeed.setPublicationDate(oldRSSFeed.getPublicationDate());
            rssFeedRepository.save(oldRSSFeed);
        }*/
        return Optional.of(updateRSSFeed);
    }

    public String deleteRSSFeedById(Long id) {
        return "";
    }

}
