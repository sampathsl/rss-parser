/**
 * @author  Sampath Thennakoon
 * @version 1.0
 * @since   2021-10-20
 */

package com.gifted.rss.exception;

public class ConnectionErrorException extends Exception {
    public ConnectionErrorException(String errorMessage) {
        super(errorMessage);
    }
}
