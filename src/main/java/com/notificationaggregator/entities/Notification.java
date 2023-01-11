package com.notificationaggregator.entities;

import com.fasterxml.jackson.databind.JsonNode;

public class Notification {
    private JsonNode eventTimestamp;
    private JsonNode eventID;
    private JsonNode organisationID;
    private JsonNode eventType;
    private String eventSource;
    private JsonNode description;
    private JsonNode severityLevel;
    private JsonNode severityValue;


    public JsonNode getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(JsonNode eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public JsonNode getEventID() {
        return eventID;
    }

    public void setEventID(JsonNode eventID) {
        this.eventID = eventID;
    }

    public JsonNode getOrganisationID() {
        return organisationID;
    }

    public void setOrganisationID(JsonNode organisationID) {
        this.organisationID = organisationID;
    }

    public JsonNode getEventType() {
        return eventType;
    }

    public void setEventType(JsonNode eventType) {
        this.eventType = eventType;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public JsonNode getDescription() {
        return description;
    }

    public void setDescription(JsonNode description) {
        this.description = description;
    }

    public JsonNode getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(JsonNode severityLevel) {
        this.severityLevel = severityLevel;
    }

    public JsonNode getSeverityValue() {
        return severityValue;
    }

    public void setSeverityValue(JsonNode severityValue) {
        this.severityValue = severityValue;
    }

}