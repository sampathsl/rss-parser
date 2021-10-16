package com.gifted.rss.repository;

import com.gifted.rss.entity.RSSFeed;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RSSFeedRepository extends PagingAndSortingRepository<RSSFeed, Long> {

    @Transactional(readOnly = true)
    @Cacheable("rss_feeds")
    Page<RSSFeed> findAll(Pageable pageable) throws DataAccessException;

}
