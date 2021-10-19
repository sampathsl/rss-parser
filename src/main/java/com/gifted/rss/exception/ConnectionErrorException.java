package com.gifted.rss.exception;

public class ConnectionErrorException extends Exception {
    public ConnectionErrorException(String errorMessage) {
        super(errorMessage);
    }
}
