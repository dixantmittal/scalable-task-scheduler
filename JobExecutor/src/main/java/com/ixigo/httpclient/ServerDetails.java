package com.ixigo.httpclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 19/04/17.
 */
@Setter
@Getter
@Component
public class ServerDetails {
    @Value("${jobscheduler.server.ip}")
    private String serverIp;

    @Value("${jobscheduler.server.port}")
    private String serverPort;

    @Value("${jobscheduler.api.uri}")
    private String apiUri;
}
