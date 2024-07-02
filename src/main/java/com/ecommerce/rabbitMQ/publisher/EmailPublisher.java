package com.ecommerce.rabbitMQ.publisher;

import com.ecommerce.decorators.EmailDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailPublisher {

    private final Connection connection;
    private final String QUEUE;

    public EmailPublisher(Connection connection,@Value("${spring.rabbitmq.queue.name}")
    String QUEUE) {
        this.connection = connection;
        this.QUEUE = QUEUE;
    }

    public void sendEmailNotification(EmailDetails emailDetails)  {
        try{
            byte[] bytes = new ObjectMapper().writeValueAsBytes(emailDetails);
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE, true, false, false, null);
            channel.basicPublish("", QUEUE, null, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
