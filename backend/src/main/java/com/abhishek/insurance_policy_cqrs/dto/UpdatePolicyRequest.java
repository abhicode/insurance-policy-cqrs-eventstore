package com.abhishek.insurance_policy_cqrs.dto;

import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdatePolicyRequest {

    @NotBlank
    private String name;

    @NotNull
    private PolicyStatus status;

    @NotNull
    private LocalDate coverageStartDate;

    @NotNull
    private LocalDate coverageEndDate;

    public UpdatePolicyRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public LocalDate getCoverageStartDate() {
        return coverageStartDate;
    }

    public void setCoverageStartDate(LocalDate coverageStartDate) {
        this.coverageStartDate = coverageStartDate;
    }

    public LocalDate getCoverageEndDate() {
        return coverageEndDate;
    }

    public void setCoverageEndDate(LocalDate coverageEndDate) {
        this.coverageEndDate = coverageEndDate;
    }
}
