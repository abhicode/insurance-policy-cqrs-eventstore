package com.abhishek.insurance_policy_cqrs.repository;

import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Integer> {

}
