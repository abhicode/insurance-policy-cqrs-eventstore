package com.abhishek.insurance_policy_cqrs.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EventStoreService {

    private final EventRepository repository;
    private final ObjectMapper mapper;

    public EventStoreService(EventRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void appendEvent(Integer aggregateId, Object event) {
        try {
            String payload = mapper.writeValueAsString(event);
            String eventType = event.getClass().getSimpleName();
            EventDocument doc = new EventDocument(aggregateId, eventType, Instant.now(), payload);
            repository.save(doc);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
