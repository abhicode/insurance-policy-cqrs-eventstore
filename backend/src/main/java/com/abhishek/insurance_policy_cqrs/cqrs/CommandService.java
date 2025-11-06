package com.abhishek.insurance_policy_cqrs.cqrs;

import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;
// import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;
import com.abhishek.insurance_policy_cqrs.dto.CreatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.dto.UpdatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.events.EventStoreService;
import com.abhishek.insurance_policy_cqrs.repository.InsurancePolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommandService {

    private final InsurancePolicyRepository repository;
    private final EventStoreService eventStore;

    public CommandService(InsurancePolicyRepository repository, EventStoreService eventStore) {
        this.repository = repository;
        this.eventStore = eventStore;
    }

    @Transactional
    public InsurancePolicy createPolicy(CreatePolicyRequest req) {
        // Basic validation: coverage dates
        if (req.getCoverageEndDate().isBefore(req.getCoverageStartDate())) {
            throw new IllegalArgumentException("coverageEndDate must be after coverageStartDate");
        }

        InsurancePolicy policy = new InsurancePolicy(req.getName(), req.getStatus(), req.getCoverageStartDate(), req.getCoverageEndDate());
        InsurancePolicy saved = repository.save(policy);

        // append event
        PolicyEvents.PolicyCreated ev = new PolicyEvents.PolicyCreated(saved.getId(), saved.getName(), saved.getStatus().name(), saved.getCoverageStartDate(), saved.getCoverageEndDate());
        eventStore.appendEvent(saved.getId(), ev);

        return saved;
    }

    @Transactional
    public InsurancePolicy updatePolicy(Integer id, UpdatePolicyRequest req) {
        Optional<InsurancePolicy> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Policy not found: " + id);
        }

        if (req.getCoverageEndDate().isBefore(req.getCoverageStartDate())) {
            throw new IllegalArgumentException("coverageEndDate must be after coverageStartDate");
        }

        InsurancePolicy p = existing.get();
        p.setName(req.getName());
        p.setStatus(req.getStatus());
        p.setCoverageStartDate(req.getCoverageStartDate());
        p.setCoverageEndDate(req.getCoverageEndDate());

        InsurancePolicy saved = repository.save(p);

        PolicyEvents.PolicyUpdated ev = new PolicyEvents.PolicyUpdated(saved.getId(), saved.getName(), saved.getStatus().name(), saved.getCoverageStartDate(), saved.getCoverageEndDate());
        eventStore.appendEvent(saved.getId(), ev);

        return saved;
    }
}
