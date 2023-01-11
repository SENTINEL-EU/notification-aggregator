package com.notificationaggregator.controller;

import com.notificationaggregator.entities.Notification;
import com.notificationaggregator.services.RabbitMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class EventController {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public EventController(ConnectionFactory connectionFactory) {
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
    }

    @Autowired
    private ConnectionFactory connectionFactory;

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getEvents() throws IOException {

        RabbitMQListener rabbitMQListener = new RabbitMQListener();
        List<Notification> events = new ArrayList<>();
        List<Object> messages = new ArrayList<>();

        try {
            return rabbitMQListener.processMessage(exchange, routingKey, events, rabbitTemplate, queue, messages);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}