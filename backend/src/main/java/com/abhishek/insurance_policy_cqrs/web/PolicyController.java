package com.abhishek.insurance_policy_cqrs.web;

import com.abhishek.insurance_policy_cqrs.cqrs.CommandService;
import com.abhishek.insurance_policy_cqrs.cqrs.QueryService;
import com.abhishek.insurance_policy_cqrs.dto.CreatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.dto.PolicyDto;
import com.abhishek.insurance_policy_cqrs.dto.UpdatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final CommandService commandService;
    private final QueryService queryService;

    public PolicyController(CommandService commandService, QueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public List<PolicyDto> list() {
        return queryService.listPolicies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyDto> get(@PathVariable Integer id) {
        PolicyDto dto = queryService.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PolicyDto> create(@Validated @RequestBody CreatePolicyRequest req) {
        InsurancePolicy created = commandService.createPolicy(req);
        PolicyDto dto = queryService.getById(created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicyDto> update(@PathVariable Integer id, @Validated @RequestBody UpdatePolicyRequest req) {
        try {
            InsurancePolicy updated = commandService.updatePolicy(id, req);
            PolicyDto dto = queryService.getById(updated.getId());
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
