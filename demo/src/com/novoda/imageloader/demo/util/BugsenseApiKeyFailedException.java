package com.novoda.imageloader.demo.util;

public class BugsenseApiKeyFailedException extends Exception {

    private String message;

    public BugsenseApiKeyFailedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
