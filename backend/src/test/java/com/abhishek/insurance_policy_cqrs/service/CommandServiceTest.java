package com.abhishek.insurance_policy_cqrs.service;

import com.abhishek.insurance_policy_cqrs.cqrs.CommandService;
import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;
import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;
import com.abhishek.insurance_policy_cqrs.dto.CreatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.dto.UpdatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.events.EventStoreService;
import com.abhishek.insurance_policy_cqrs.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

    @Mock
    private InsurancePolicyRepository repository;

    @Mock
    private EventStoreService eventStore;

    private CommandService commandService;

    @BeforeEach
    void setUp() {
        commandService = new CommandService(repository, eventStore);
    }

    @Test
    void createPolicy_ValidRequest_ShouldSaveAndAppendEvent() {
        // given
        CreatePolicyRequest req = new CreatePolicyRequest();
        req.setName("Test Policy");
        req.setStatus(PolicyStatus.ACTIVE);
        req.setCoverageStartDate(LocalDate.now());
        req.setCoverageEndDate(LocalDate.now().plusYears(1));

        InsurancePolicy savedPolicy = new InsurancePolicy(req.getName(), req.getStatus(), req.getCoverageStartDate(), req.getCoverageEndDate());
        savedPolicy.setId(1);

        when(repository.save(any(InsurancePolicy.class))).thenReturn(savedPolicy);

        // when
        InsurancePolicy result = commandService.createPolicy(req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo(req.getName());
        verify(repository).save(any(InsurancePolicy.class));
        verify(eventStore).appendEvent(eq(1), any());
    }

    @Test
    void createPolicy_InvalidDates_ShouldThrowException() {
        // given
        CreatePolicyRequest req = new CreatePolicyRequest();
        req.setName("Test Policy");
        req.setStatus(PolicyStatus.ACTIVE);
        req.setCoverageStartDate(LocalDate.now().plusDays(1));
        req.setCoverageEndDate(LocalDate.now()); // end before start

        // when/then
        assertThatThrownBy(() -> commandService.createPolicy(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("coverageEndDate must be after coverageStartDate");

        verify(repository, never()).save(any());
        verify(eventStore, never()).appendEvent(any(), any());
    }

    @Test
    void updatePolicy_ValidRequest_ShouldUpdateAndAppendEvent() {
        // given
        Integer policyId = 1;
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        req.setName("Updated Policy");
        req.setStatus(PolicyStatus.INACTIVE);
        req.setCoverageStartDate(LocalDate.now());
        req.setCoverageEndDate(LocalDate.now().plusYears(1));

        InsurancePolicy existingPolicy = new InsurancePolicy("Old Name", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1));
        existingPolicy.setId(policyId);

        when(repository.findById(policyId)).thenReturn(Optional.of(existingPolicy));
        when(repository.save(any(InsurancePolicy.class))).thenReturn(existingPolicy);

        // when
        InsurancePolicy result = commandService.updatePolicy(policyId, req);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(req.getName());
        assertThat(result.getStatus()).isEqualTo(req.getStatus());
        verify(repository).save(any(InsurancePolicy.class));
        verify(eventStore).appendEvent(eq(policyId), any());
    }

    @Test
    void updatePolicy_NonExistentPolicy_ShouldThrowException() {
        // given
        Integer policyId = 999;
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        req.setName("Updated Policy");
        req.setStatus(PolicyStatus.ACTIVE);
        req.setCoverageStartDate(LocalDate.now());
        req.setCoverageEndDate(LocalDate.now().plusYears(1));

        when(repository.findById(policyId)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> commandService.updatePolicy(policyId, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Policy not found");

        verify(repository, never()).save(any());
        verify(eventStore, never()).appendEvent(any(), any());
    }
}