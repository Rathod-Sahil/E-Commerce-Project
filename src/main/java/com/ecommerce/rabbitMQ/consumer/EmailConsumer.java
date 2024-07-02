package com.ecommerce.rabbitMQ.consumer;

import com.ecommerce.decorators.EmailDetails;
import com.ecommerce.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class EmailConsumer {

    private final EmailService emailService;
    
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.queue.name}"),
            exchange = @Exchange(value = "${spring.rabbitmq.template.exchange}"),
            key = "${spring.rabbitmq.template.routing-key}"
    ))
    public void receiveEmailNotification(byte[] data) throws IOException {
        EmailDetails emailDetails = new ObjectMapper().readValue(data, EmailDetails.class);
        emailService.sendEmail(emailDetails.getRecipient(), emailDetails.getSubject(), emailDetails.getContent());
    }
}

