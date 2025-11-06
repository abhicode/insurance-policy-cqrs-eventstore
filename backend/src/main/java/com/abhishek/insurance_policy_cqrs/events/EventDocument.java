package com.abhishek.insurance_policy_cqrs.events;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "policy_events")
public class EventDocument {

    @Id
    private String id;

    private Integer aggregateId;

    private String eventType;

    private Instant occurredAt;

    private String payload;

    public EventDocument() {
    }

    public EventDocument(Integer aggregateId, String eventType, Instant occurredAt, String payload) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(Integer aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
