package com.gifted.rss.controller;

import com.gifted.rss.service.RSSFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class RSSFeedController {

    Logger logger = LoggerFactory.getLogger(RSSFeedController.class);

    @Autowired
    private RSSFeedService rssFeedService;

    @GetMapping("/")
    public String welcome() {
        return "RSS Feed Parser is Up and Running!";
    }

    //GET http://<host>:<port>/items?page=1&size=2&sort=updated_date&direction=asc
    @GetMapping("/items")
    public String getItems(@RequestParam(required = false, defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "10") Integer size,
                           @RequestParam(required = false, defaultValue = "updated_date") String sortBy,
                           @RequestParam(required = false, defaultValue = "desc") String direction) {
        logger.info(page.toString());
        logger.info(size.toString());
        logger.info(sortBy);
        logger.info(direction);
        rssFeedService.getLatestRSSFeeds(page,size,sortBy,direction);
        return "TODO!";
    }

}
