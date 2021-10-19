package com.gifted.rss.util;

import com.gifted.rss.util.enums.DIRECTION;
import com.gifted.rss.util.enums.SORTBY;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void getConstantTest() {
        Assert.assertEquals(0, Constant.DEFAULT_PAGE);
        Assert.assertEquals(10, Constant.DEFAULT_PAGE_SIZE);
        Assert.assertEquals(5000, Constant.DEFAULT_MAX_DB_FETCH_COUNT);
        Assert.assertEquals(DIRECTION.DESC.getValue(), Constant.DEFAULT_DIRECTION);
        Assert.assertEquals(SORTBY.UPDATED_DATE.getValue(), Constant.DEFAULT_SORT_BY);
    }

    @Test
    public void getEnumTest() {
        DIRECTION directionAsc = DIRECTION.ASC;
        DIRECTION directionDesc = DIRECTION.DESC;
        Assert.assertEquals("asc", directionAsc.getValue());
        Assert.assertEquals("desc", directionDesc.getValue());
    }

}
