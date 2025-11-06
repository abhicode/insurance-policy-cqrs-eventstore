package com.abhishek.insurance_policy_cqrs.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "insurance_policy")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    @Column(name = "coverage_start_date", nullable = false)
    private LocalDate coverageStartDate;

    @Column(name = "coverage_end_date", nullable = false)
    private LocalDate coverageEndDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private Instant creationDate;

    @Column(name = "last_updated_date", nullable = false)
    private Instant lastUpdatedDate;

    public InsurancePolicy() {
    }

    public InsurancePolicy(String name, PolicyStatus status, LocalDate coverageStartDate, LocalDate coverageEndDate) {
        this.name = name;
        this.status = status;
        this.coverageStartDate = coverageStartDate;
        this.coverageEndDate = coverageEndDate;
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.creationDate = now;
        this.lastUpdatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedDate = Instant.now();
    }

    // getters and setters

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
