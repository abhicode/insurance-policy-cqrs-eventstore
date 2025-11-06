package com.abhishek.insurance_policy_cqrs.dto;

import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PolicyDtoTest {

    @Test
    void gettersAndSetters_shouldWork() {
        PolicyDto dto = new PolicyDto();

        dto.setId(42);
        dto.setName("My Policy");
        dto.setStatus(PolicyStatus.ACTIVE);
        dto.setCoverageStartDate(LocalDate.of(2025, 11, 5));
        dto.setCoverageEndDate(LocalDate.of(2025, 12, 31));
        Instant now = Instant.now();
        dto.setCreationDate(now);
        dto.setLastUpdatedDate(now);

        assertThat(dto.getId()).isEqualTo(42);
        assertThat(dto.getName()).isEqualTo("My Policy");
        assertThat(dto.getStatus()).isEqualTo(PolicyStatus.ACTIVE);
        assertThat(dto.getCoverageStartDate()).isEqualTo(LocalDate.of(2025, 11, 5));
        assertThat(dto.getCoverageEndDate()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(dto.getCreationDate()).isEqualTo(now);
        assertThat(dto.getLastUpdatedDate()).isEqualTo(now);
    }

    @Test
    void allArgsConstructor_shouldPopulateFields() {
        Instant created = Instant.parse("2025-11-05T10:15:30.00Z");
        Instant updated = Instant.parse("2025-11-05T12:00:00.00Z");

        PolicyDto dto = new PolicyDto(7, "Ctor Policy", PolicyStatus.INACTIVE,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 6, 30), created, updated);

        assertThat(dto.getId()).isEqualTo(7);
        assertThat(dto.getName()).isEqualTo("Ctor Policy");
        assertThat(dto.getStatus()).isEqualTo(PolicyStatus.INACTIVE);
        assertThat(dto.getCoverageStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(dto.getCoverageEndDate()).isEqualTo(LocalDate.of(2025, 6, 30));
        assertThat(dto.getCreationDate()).isEqualTo(created);
        assertThat(dto.getLastUpdatedDate()).isEqualTo(updated);
    }
}
