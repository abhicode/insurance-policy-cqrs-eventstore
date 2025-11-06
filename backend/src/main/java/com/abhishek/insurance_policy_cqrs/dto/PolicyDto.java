package com.abhishek.insurance_policy_cqrs.dto;

import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;

import java.time.Instant;
import java.time.LocalDate;

public class PolicyDto {
    private Integer id;
    private String name;
    private PolicyStatus status;
    private LocalDate coverageStartDate;
    private LocalDate coverageEndDate;
    private Instant creationDate;
    private Instant lastUpdatedDate;

    public PolicyDto() {
    }

    public PolicyDto(Integer id, String name, PolicyStatus status, LocalDate coverageStartDate, LocalDate coverageEndDate, Instant creationDate, Instant lastUpdatedDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.coverageStartDate = coverageStartDate;
        this.coverageEndDate = coverageEndDate;
        this.creationDate = creationDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Instant lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}
