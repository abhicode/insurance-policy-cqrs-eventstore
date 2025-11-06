package com.abhishek.insurance_policy_cqrs.integration;

import com.abhishek.insurance_policy_cqrs.cqrs.CommandService;
import com.abhishek.insurance_policy_cqrs.cqrs.QueryService;
import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;
import com.abhishek.insurance_policy_cqrs.dto.CreatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.dto.PolicyDto;
import com.abhishek.insurance_policy_cqrs.dto.UpdatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.events.EventDocument;
import com.abhishek.insurance_policy_cqrs.events.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class InsurancePolicyIntegrationTest {

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("insurance_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:7");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.mongodb.uri", mongodb::getReplicaSetUrl);
    }

    @Autowired
    private CommandService commandService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void fullLifecycle_CreateUpdateAndQueryPolicy() {
        // Create a policy
        CreatePolicyRequest createReq = new CreatePolicyRequest();
        createReq.setName("Test Policy");
        createReq.setStatus(PolicyStatus.ACTIVE);
        createReq.setCoverageStartDate(LocalDate.now());
        createReq.setCoverageEndDate(LocalDate.now().plusYears(1));

        var created = commandService.createPolicy(createReq);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Policy");

        // Verify policy is queryable
        PolicyDto queried = queryService.getById(created.getId());
        assertThat(queried).isNotNull();
        assertThat(queried.getName()).isEqualTo("Test Policy");
        assertThat(queried.getStatus()).isEqualTo(PolicyStatus.ACTIVE);

        // Update the policy
        UpdatePolicyRequest updateReq = new UpdatePolicyRequest();
        updateReq.setName("Updated Policy");
        updateReq.setStatus(PolicyStatus.INACTIVE);
        updateReq.setCoverageStartDate(LocalDate.now());
        updateReq.setCoverageEndDate(LocalDate.now().plusYears(2));

        var updated = commandService.updatePolicy(created.getId(), updateReq);
        assertThat(updated.getName()).isEqualTo("Updated Policy");
        assertThat(updated.getStatus()).isEqualTo(PolicyStatus.INACTIVE);

        // Verify updates are reflected in query
        PolicyDto queriedAfterUpdate = queryService.getById(created.getId());
        assertThat(queriedAfterUpdate.getName()).isEqualTo("Updated Policy");
        assertThat(queriedAfterUpdate.getStatus()).isEqualTo(PolicyStatus.INACTIVE);

        // Verify events were stored
        List<EventDocument> events = eventRepository.findAll();
        assertThat(events).hasSize(2); // One create event, one update event
        assertThat(events.get(0).getEventType()).contains("PolicyCreated");
        assertThat(events.get(1).getEventType()).contains("PolicyUpdated");
    }

    @Test
    void createPolicy_InvalidDates_ShouldFail() {
        CreatePolicyRequest req = new CreatePolicyRequest();
        req.setName("Invalid Policy");
        req.setStatus(PolicyStatus.ACTIVE);
        req.setCoverageStartDate(LocalDate.now().plusDays(2));
        req.setCoverageEndDate(LocalDate.now().plusDays(1)); // End before start

        assertThat(req.getCoverageEndDate()).isBefore(req.getCoverageStartDate());

        try {
            commandService.createPolicy(req);
            throw new AssertionError("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).contains("coverageEndDate must be after coverageStartDate");
        }

        // Verify no events were stored for failed operation
        List<EventDocument> events = eventRepository.findAll();
        assertThat(events).filteredOn(e -> e.getPayload().contains("Invalid Policy")).isEmpty();
    }
}