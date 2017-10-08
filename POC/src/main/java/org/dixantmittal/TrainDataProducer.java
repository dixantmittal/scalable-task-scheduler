package org.dixantmittal;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.dixantmittal.entity.Task;

import java.util.Properties;
import java.util.Random;

/**
 * Created by dixant on 07/07/17.
 */
public class TrainDataProducer {
    private static Producer<String, Task> producer;
    private static String[] trains = {"12345", "13456", "14567", "15678", "16789", "17890", "13579", "12468", "14680"};

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.dixantmittal.TaskSerializer");
        producer = new KafkaProducer<>(props);
    }

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        while (true) {
            int index = random.nextInt(9);
            producer.send(new ProducerRecord<String, Task>("train_codes", trains[index], new Task(null, "TRAIN", "abcd", null, null)));
            System.out.println(trains[index]);
            Thread.sleep(500);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        producer.close();
    }
}