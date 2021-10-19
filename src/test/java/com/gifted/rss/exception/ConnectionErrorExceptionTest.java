package com.gifted.rss.exception;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionErrorExceptionTest {

    private Logger logger = LoggerFactory.getLogger(ConnectionErrorExceptionTest.class);
    private static String CONTENT_NOT_FOUND_MSG = "Could not able to connect RSS Feed server!";

    @Test
    public void connectionErrorExceptionTest() {
        logger.info("Content Not Found Exception Test");
        ConnectionErrorException connectionErrorException = new ConnectionErrorException(CONTENT_NOT_FOUND_MSG);
        Assert.assertNotNull(connectionErrorException);
        Assert.assertEquals(CONTENT_NOT_FOUND_MSG, connectionErrorException.getMessage());
    }

}