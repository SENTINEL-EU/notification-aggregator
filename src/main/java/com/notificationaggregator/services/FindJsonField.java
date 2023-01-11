package com.notificationaggregator.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class FindJsonField {

    public JsonNode findNestedField(JsonNode node, String fieldName) {
        if (node.has(fieldName)) {
            return node.get(fieldName);
        } else {
            for (JsonNode childNode : node) {
                JsonNode value = findNestedField(childNode, fieldName);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }
}