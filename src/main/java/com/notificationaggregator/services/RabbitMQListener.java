package com.notificationaggregator.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationaggregator.entities.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@RabbitListener(queues = "${rabbitmq.queue.name}", containerFactory = "rabbitListenerContainerFactory")
public class RabbitMQListener {
    public ResponseEntity<List<Notification>> processMessage(String exchange, String routingKey, List<Notification> events, RabbitTemplate rabbitTemplate, String queue, List<Object> messages) {
        log.info("Consuming Messages...");
        int counter = 0;

        try {
            Object message = rabbitTemplate.receiveAndConvert(queue);
            messages.add(message);

            while (message != null) {

                counter++;

                FindJsonField findJsonField = new FindJsonField();
                Notification transformNotification = new Notification();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(message.toString());

                transformNotification.setEventTimestamp(findJsonField.findNestedField(root, "timestamp"));
                transformNotification.setEventID(findJsonField.findNestedField(root, "eventID"));
                transformNotification.setEventType(findJsonField.findNestedField(root, "channel"));
                transformNotification.setEventSource("Security Infusion");
                transformNotification.setDescription(findJsonField.findNestedField(root, "message"));
                transformNotification.setSeverityLevel(root.path("rule").path("level"));
                transformNotification.setSeverityValue(findJsonField.findNestedField(root, "severityValue"));
                transformNotification.setOrganisationID(findJsonField.findNestedField(root, "organisationID"));

                try {
                    String filter = root.path("rule").path("level").toString().replace("\"", "");
                    int result = Integer.parseInt(filter);
                    if (result>=7) {
                        events.add(transformNotification);
                    }
                }
                catch(Exception e){
                       log.info("The value of the field level is " + e.getMessage());

                }
                message = rabbitTemplate.receiveAndConvert(queue);
                messages.add(message);
            }
            for (int i = 0; i < counter; i++) {
                rabbitTemplate.convertAndSend(exchange, routingKey, messages.get(i).toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        log.info("Consumed all " + counter + " messages successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }
}
