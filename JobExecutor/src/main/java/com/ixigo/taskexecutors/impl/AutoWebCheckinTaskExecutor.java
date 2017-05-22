package com.ixigo.taskexecutors.impl;

import com.google.gson.JsonSyntaxException;
import com.ixigo.taskexecutors.AbstractTaskExecutor;
import com.ixigo.utils.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by dixant on 18/04/17.
 */
@Slf4j
@Component(value = "autoWebCheckinTaskExecutor")
public class AutoWebCheckinTaskExecutor extends AbstractTaskExecutor {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    @Override
    public Boolean process(String meta) throws JsonSyntaxException {
        String _QUEUE_NAME = Configuration.getProperty("rabbitmq.properties", "queue.name");
        String _SERVER_ADDRESS = Configuration.getProperty("rabbitmq.properties", "server.address");
        log.debug("RabbitMQ Queue Name: {}", _QUEUE_NAME);
        log.debug("RabbitMQ Server address: {}", _SERVER_ADDRESS);
        try {
            factory = new ConnectionFactory();
            factory.setHost(_SERVER_ADDRESS);
            connection = factory.newConnection();
            channel = connection.createChannel();
            log.info("RabbitMQ channel created successfully. Sending data...");
            channel.queueDeclare(_QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", _QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, meta.getBytes());
            log.info("Data sent successfully to RabbitMQ");
            return true;
        } catch (IOException | TimeoutException e) {
            log.error("Error occurred while sending data to RabbitMQ. ", e);
            return false;
        } finally {
            try {
                channel.close();
                log.info("RabbitMQ channel closed.");
                connection.close();
                log.info("RabbitMQ connection closed.");
            } catch (IOException | TimeoutException e) {
                log.error("Error occurred while closing connection to RabbitMQ. ", e);
                return false;
            }
        }
    }


    // Code to read from RabbitMQ for testing
//    private void testReceive() {
//        String _QUEUE_NAME = "test";//Configuration.getProperty("rabbitmq.properties", "queue.name");
//        String _SERVER_ADDRESS = "localhost";//Configuration.getProperty("rabbitmq.properties", "server.address");
//
//        try {
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost(_SERVER_ADDRESS);
//            Connection connection = factory.newConnection();
//            Channel channel = connection.createChannel();
//
//            channel.queueDeclare(_QUEUE_NAME, false, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//            Consumer consumer = new DefaultConsumer(channel) {
//                @Override
//                public void handleDelivery(String consumerTag, Envelope envelope,
//                                           AMQP.BasicProperties properties, byte[] body)
//                        throws IOException {
//                    String message = new String(body, "UTF-8");
//                    System.out.println(" [x] Received '" + message + "'");
//                }
//            };
//            channel.basicConsume(_QUEUE_NAME, true, consumer);
//        } catch (TimeoutException | IOException e) {
//            e.printStackTrace();
//        }
//    }
}
