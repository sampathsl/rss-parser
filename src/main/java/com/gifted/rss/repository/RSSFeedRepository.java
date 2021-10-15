package com.gifted.rss.repository;

import com.gifted.rss.entity.RSSFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RSSFeedRepository extends PagingAndSortingRepository<RSSFeed, Long> {

    @Query(value = "SELECT link,title,description,publication_date,updated_date FROM rss_feed", nativeQuery=true)
    @Transactional(readOnly = true)
    Page<RSSFeed> findLatestRSSFeeds(Pageable pageable);

}
