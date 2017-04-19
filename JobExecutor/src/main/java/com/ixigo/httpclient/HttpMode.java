package com.ixigo.httpclient;

/**
 * Created by dixant on 18/04/17.
 */
public enum HttpMode {
    HTTP("http://"),
    HTTPS("https://");
    private final String value;

    HttpMode(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
