package com.gifted.rss.exception;

import com.gifted.rss.error.APIError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RSSFeedExceptionController {

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleCommonException(Exception ex, WebRequest request) {
        APIError apiError = new APIError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Internal server error occurred");
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

}
