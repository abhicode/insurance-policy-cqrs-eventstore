package com.abhishek.insurance_policy_cqrs.service;

import com.abhishek.insurance_policy_cqrs.cqrs.QueryService;
import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;
import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;
import com.abhishek.insurance_policy_cqrs.dto.PolicyDto;
import com.abhishek.insurance_policy_cqrs.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

    @Mock
    private InsurancePolicyRepository repository;

    private QueryService queryService;

    @BeforeEach
    void setUp() {
        queryService = new QueryService(repository);
    }

    @Test
    void listPolicies_ShouldReturnAllPolicies() {
        // given
        InsurancePolicy policy1 = new InsurancePolicy("Policy 1", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1));
        policy1.setId(1);
        InsurancePolicy policy2 = new InsurancePolicy("Policy 2", PolicyStatus.INACTIVE, LocalDate.now(), LocalDate.now().plusYears(2));
        policy2.setId(2);

        when(repository.findAll()).thenReturn(Arrays.asList(policy1, policy2));

        // when
        List<PolicyDto> result = queryService.listPolicies();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Policy 1");
        assertThat(result.get(0).getStatus()).isEqualTo(PolicyStatus.ACTIVE);
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getName()).isEqualTo("Policy 2");
        assertThat(result.get(1).getStatus()).isEqualTo(PolicyStatus.INACTIVE);
    }

    @Test
    void getById_ExistingPolicy_ShouldReturnPolicy() {
        // given
        InsurancePolicy policy = new InsurancePolicy("Test Policy", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1));
        policy.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(policy));

        // when
        PolicyDto result = queryService.getById(1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Test Policy");
        assertThat(result.getStatus()).isEqualTo(PolicyStatus.ACTIVE);
    }

    @Test
    void getById_NonExistentPolicy_ShouldReturnNull() {
        // given
        when(repository.findById(999)).thenReturn(Optional.empty());

        // when
        PolicyDto result = queryService.getById(999);

        // then
        assertThat(result).isNull();
    }
}