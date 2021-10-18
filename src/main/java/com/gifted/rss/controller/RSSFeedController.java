package com.gifted.rss.controller;

import com.gifted.rss.dto.RSSFeedDto;
import com.gifted.rss.service.RSSFeedService;
import com.gifted.rss.util.Constant;
import com.gifted.rss.util.DataIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/items")
    public Page<RSSFeedDto> getItems(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                     @RequestParam(name = "sortBy", required = false, defaultValue = "updatedDate")
                                     @DataIn(anyOf = {"id", "link", "title", "description", "publicationDate", "updatedDate"}) String sortBy,
                                     @RequestParam(name = "direction", required = false, defaultValue = "desc")
                                     @DataIn(anyOf = {"asc", "desc"}) String direction) {
        return rssFeedService.getLatestRSSData(page, size, sortBy, direction);
    }

}
