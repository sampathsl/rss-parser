/**
 * @author  Sampath Thennakoon
 * @version 1.0
 * @since   2021-10-20
 */

package com.gifted.rss.exception;

import com.gifted.rss.error.APIError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RSSFeedExceptionHandler {

    /**
     * Global common exception handler
     * @param ex the incoming {@link Exception} object
     * @param request the incoming {@link WebRequest} object
     * @return the {@link ResponseEntity} object with exception details
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleCommonException(Exception ex, WebRequest request) {
        APIError apiError = new APIError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Internal server error occurred");
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

}
