package com.ixigo.httpclient;

/**
 * Created by dixant on 18/04/17.
 */
public enum RequestHeaders {
    CONTENT_TYPE("content-type"),
    ACCEPT("accept"),
    CLIENT_IP_ADDRESS("clientIpAddress"),
    USER_AGENT("user-Agent"),
    USER_MACHINE_IDENTIFIER("userMachineIdentifier"),
    HTTPMETHOD("httpmethod"),
    SERVER_IP_ADDRESS("serverIpAddress"),
    USER_AGENT_NAME("user_agent_name"),
    USER_IP("user_ip"),
    LAN_IP("lan_ip"),
    OS_VERSION("os_version");

    private String description;

    private RequestHeaders(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
