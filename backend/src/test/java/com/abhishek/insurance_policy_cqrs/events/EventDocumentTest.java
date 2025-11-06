package com.abhishek.insurance_policy_cqrs.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class EventDocumentTest {

    @Test
    void constructorAndGetters_shouldWork() {
        Instant now = Instant.parse("2025-11-05T10:00:00Z");
        EventDocument doc = new EventDocument(123, "PolicyCreated", now, "{\"name\":\"Test\"}");

        assertThat(doc.getAggregateId()).isEqualTo(123);
        assertThat(doc.getEventType()).isEqualTo("PolicyCreated");
        assertThat(doc.getOccurredAt()).isEqualTo(now);
        assertThat(doc.getPayload()).isEqualTo("{\"name\":\"Test\"}");
    }

    @Test
    void settersAndGetters_shouldWork() {
        EventDocument doc = new EventDocument();
        doc.setId("abc-123");
        doc.setAggregateId(555);
        doc.setEventType("PolicyUpdated");
        Instant t = Instant.now();
        doc.setOccurredAt(t);
        doc.setPayload("{\"updated\":true}");

        assertThat(doc.getId()).isEqualTo("abc-123");
        assertThat(doc.getAggregateId()).isEqualTo(555);
        assertThat(doc.getEventType()).isEqualTo("PolicyUpdated");
        assertThat(doc.getOccurredAt()).isEqualTo(t);
        assertThat(doc.getPayload()).isEqualTo("{\"updated\":true}");
    }
}
