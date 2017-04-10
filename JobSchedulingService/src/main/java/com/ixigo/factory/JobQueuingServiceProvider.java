package com.ixigo.factory;

import com.ixigo.service.IJobQueuingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 28/03/17.
 */
public class JobQueuingServiceProvider {

    private static JobQueuingServiceProvider _INSTANCE = new JobQueuingServiceProvider();

    @Autowired
    @Qualifier("kafkaJobQueuingService")
    IJobQueuingService kafkaJobQueuingService;

    public static JobQueuingServiceProvider getInstance() {
        return _INSTANCE;
    }

    public IJobQueuingService getJobQueuingService() {
        return kafkaJobQueuingService;
    }
}
