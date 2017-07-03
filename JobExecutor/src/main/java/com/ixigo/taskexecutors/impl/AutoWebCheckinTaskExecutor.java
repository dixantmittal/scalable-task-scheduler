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
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Created by dixant on 18/04/17.
 */
@Slf4j
@Component(value = "autoWebCheckinTaskExecutor")
public class AutoWebCheckinTaskExecutor extends AbstractTaskExecutor {
    @Override
    public Boolean process(String meta) throws JsonSyntaxException {
        ConnectionFactory factory;
        Connection connection = null;
        Channel channel = null;
        String _QUEUE_NAME = Configuration.getProperty("rabbitmq.properties", "queue.name");
        String _SERVER_URI = Configuration.getProperty("rabbitmq.properties", "server.uri");
        log.info("RabbitMQ Queue Name: {}", _QUEUE_NAME);
        log.info("RabbitMQ Server uri: {}", _SERVER_URI);
        try {
            factory = new ConnectionFactory();
            factory.setUri(_SERVER_URI);
            connection = factory.newConnection();
            channel = connection.createChannel();
            log.info("RabbitMQ channel created successfully. Sending data...");
            channel.queueDeclare(_QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", _QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, meta.getBytes());
            log.info("Data sent successfully to RabbitMQ");
            return true;
        } catch (IOException | TimeoutException | NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
            log.error("Error occurred while sending data to RabbitMQ. ", e);
            return false;
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                    log.info("RabbitMQ channel closed.");
                }
                if (connection != null) {
                connection.close();
                    log.info("RabbitMQ connection closed.");
                }
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
