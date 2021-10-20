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

@Service
public class RSSFeedServiceImpl implements RSSFeedService {

    private RSSFeedRepository rssFeedRepository;

    @Autowired
    public RSSFeedServiceImpl(RSSFeedRepository rssFeedRepository) {
        this.rssFeedRepository = rssFeedRepository;
    }

    /**
     * Add given RSSFeeds to databse and returns the newly added RSSFeeds
     * @param rssFeeds list of RSSFeeds
     * @return list of RSSFeeds
     */
    public List<RSSFeed> addRSSFeeds(List<RSSFeed> rssFeeds) {
        return (List<RSSFeed>) rssFeedRepository.saveAll(rssFeeds);
    }

    /**
     * Get a page of latest {@link RSSFeed} for given parameters (page, size, sort by, direction)
     * @param page the integer for page number
     * @param size the integer for number of RSSFeeds in a page
     * @param sortBy the string for sorting column name ("id", "link", "title", "description", "publicationDate", "updatedDate")
     * @param direction the string for direction ("asc", "desc")
     * @return a page of {@link RSSFeed}
     */
    @Override
    public Page<RSSFeed> getLatestRSSFeeds(Integer page, Integer size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        if (direction.contentEquals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }
        return rssFeedRepository.findAll(pageable);
    }

    /**
     * Get a page of latest {@link RSSFeedDto} data for given parameters (page, size, sort by, direction)
     * @param page the integer for page number
     * @param size the integer for number of RSSFeeds in a page
     * @param sortBy the string for sorting column name ("id", "link", "title", "description", "publicationDate", "updatedDate")
     * @param direction the string for direction ("asc", "desc")
     * @return a page of {@link RSSFeedDto}
     */
    public Page<RSSFeedDto> getLatestRSSData(Integer page, Integer size, String sortBy, String direction) {
        return toPageObjectDto(getLatestRSSFeeds(page, size, sortBy, direction));
    }

    /**
     * Convert page of RSSFeeds to a page of RSSFeeds data
     * @param rssFeeds the page of RSSFeeds
     * @return a page of {@link RSSFeedDto}
     */
    public Page<RSSFeedDto> toPageObjectDto(Page<RSSFeed> rssFeeds) {
        return rssFeeds.map(this::convertToObjectDto);
    }

    /**
     * Convert a {@link RSSFeed} object in to a {@link RSSFeedDto} object
     * @param rssFeed the RSSFeed object
     * @return a {@link RSSFeedDto} object
     */
    private RSSFeedDto convertToObjectDto(RSSFeed rssFeed) {
        RSSFeedDto dto = new RSSFeedDto();
        dto.setLink(rssFeed.getLink());
        dto.setTitle(rssFeed.getTitle());
        dto.setDescription(rssFeed.getDescription());
        dto.setPublicationDate(rssFeed.getPublicationDate());
        dto.setUpdatedDate(rssFeed.getUpdatedDate());
        return dto;
    }

    /**
     * Save a given {@link RSSFeed} object
     * @param updateRSSFeed
     * @return the saved {@link RSSFeed} object
     */
    public RSSFeed updateRSSFeed(RSSFeed updateRSSFeed) {
        return rssFeedRepository.save(updateRSSFeed);
    }

}
