package org.dixantmittal;

import lombok.extern.slf4j.Slf4j;
import org.dixantmittal.entity.RequestProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by dixant on 24/03/17.
 */
@SpringBootApplication
@Slf4j
public class TaskManagerProxy {
    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("Topic name not found. Correct syntax is: java -jar TaskManagerProxy <Topic Name>");
            System.exit(0);
        }
        SpringApplication.run(TaskManagerProxy.class, args).getBean(RequestProducer.class).setTopic(args[0]);
    }
}