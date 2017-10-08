package org.dixantmittal;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 24/03/17.
 */
@SpringBootApplication
@Slf4j
public class POC implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(POC.class, args).close();
    }

    static {
        Map<String, Object> map = new HashMap<>();
        map.put("bootstrap.servers", "localhost:9092");
        map.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        map.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        template = new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<String, String>(map)
        );
    }

    private static KafkaTemplate<String, String> template;

    @Override
    public void run(String... args) throws Exception {
        this.template.send("train_codes", "foo1");
    }

    @KafkaListener(topics = "train_codes", group = "test")
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        log.info(cr.toString());
    }
}
