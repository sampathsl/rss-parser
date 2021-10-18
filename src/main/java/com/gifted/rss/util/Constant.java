package com.gifted.rss.util;

import com.gifted.rss.util.enums.DIRECTION;
import com.gifted.rss.util.enums.SORTBY;

public class Constant {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_BY = SORTBY.UPDATED_DATE.getValue();
    public static final String DEFAULT_DIRECTION = DIRECTION.DESC.getValue();
    public static final int DEFAULT_MAX_DB_FETCH_COUNT = 5000;
}
