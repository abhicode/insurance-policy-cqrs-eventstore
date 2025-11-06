package com.abhishek.insurance_policy_cqrs.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventStoreServiceTest {

    @Mock
    private EventRepository repository;

    @Mock
    private ObjectMapper mapper;

    private EventStoreService eventStoreService;

    @BeforeEach
    void setUp() {
        eventStoreService = new EventStoreService(repository, mapper);
    }

    @Test
    void appendEvent_ShouldSerializeAndSave() throws Exception {
        // given
        Integer aggregateId = 1;
        TestEvent event = new TestEvent("test data");
        String serialized = "{\"data\":\"test data\"}";

        when(mapper.writeValueAsString(event)).thenReturn(serialized);

        // when
        eventStoreService.appendEvent(aggregateId, event);

        // then
        verify(repository).save(any(EventDocument.class));
    }

    static class TestEvent {
        private String data;

        public TestEvent(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }
}