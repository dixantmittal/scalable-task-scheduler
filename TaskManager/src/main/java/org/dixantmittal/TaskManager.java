package org.dixantmittal;

import org.dixantmittal.service.ITaskManagerRequestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Created by dixant on 24/03/17.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("org.dixantmittal")
@ImportResource("application-context.xml")
public class TaskManager {
    public static void main(String[] args) {
        SpringApplication.run(TaskManager.class, args).getBean(ITaskManagerRequestService.class).startScheduler();
    }
}