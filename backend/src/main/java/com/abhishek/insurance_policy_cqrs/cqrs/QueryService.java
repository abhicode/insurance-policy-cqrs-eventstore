package com.abhishek.insurance_policy_cqrs.cqrs;

import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;
import com.abhishek.insurance_policy_cqrs.dto.PolicyDto;
import com.abhishek.insurance_policy_cqrs.repository.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {

    private final InsurancePolicyRepository repository;

    public QueryService(InsurancePolicyRepository repository) {
        this.repository = repository;
    }

    public List<PolicyDto> listPolicies() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public PolicyDto getById(Integer id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    private PolicyDto toDto(InsurancePolicy p) {
        return new PolicyDto(p.getId(), p.getName(), p.getStatus(), p.getCoverageStartDate(), p.getCoverageEndDate(), p.getCreationDate(), p.getLastUpdatedDate());
    }
}
