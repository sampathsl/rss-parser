package com.gifted.rss.error;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class APIErrorTest {

    @Test
    public void apiErrorConstructorTest() {
        List<String> errors = Arrays.asList("error1","error2");
        APIError apiErrorOne = new APIError(HttpStatus.OK, "test-message", "error");
        Assert.assertEquals(HttpStatus.OK, apiErrorOne.getStatus());
        Assert.assertEquals("test-message", apiErrorOne.getMessage());
        Assert.assertEquals(1, apiErrorOne.getErrors().size());
        APIError apiErrorTwo = new APIError(HttpStatus.OK, "test-message", errors);
        Assert.assertEquals(HttpStatus.OK, apiErrorTwo.getStatus());
        Assert.assertEquals("test-message", apiErrorTwo.getMessage());
        Assert.assertEquals(2, apiErrorTwo.getErrors().size());
    }

    @Test
    public void apiErrorSetterTest() {
        List<String> errors = Arrays.asList("error1","error2");
        APIError apiErrorOne = new APIError();
        apiErrorOne.setStatus(HttpStatus.OK);
        apiErrorOne.setMessage("test-message1");
        apiErrorOne.setErrors(errors);

        APIError apiErrorTwo = new APIError();
        apiErrorTwo.setStatus(HttpStatus.ACCEPTED);
        apiErrorTwo.setMessage("test-message2");
        apiErrorTwo.setErrors(errors);

        Assert.assertEquals(HttpStatus.OK, apiErrorOne.getStatus());
        Assert.assertEquals("test-message1", apiErrorOne.getMessage());
        Assert.assertEquals(2, apiErrorOne.getErrors().size());
        Assert.assertEquals(HttpStatus.ACCEPTED, apiErrorTwo.getStatus());
        Assert.assertEquals("test-message2", apiErrorTwo.getMessage());
        Assert.assertEquals(2, apiErrorTwo.getErrors().size());
        Assert.assertEquals(false, apiErrorOne.equals(apiErrorTwo));
    }

}
