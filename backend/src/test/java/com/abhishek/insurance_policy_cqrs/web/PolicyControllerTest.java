package com.abhishek.insurance_policy_cqrs.web;

import com.abhishek.insurance_policy_cqrs.cqrs.CommandService;
import com.abhishek.insurance_policy_cqrs.cqrs.QueryService;
import com.abhishek.insurance_policy_cqrs.domain.InsurancePolicy;
import com.abhishek.insurance_policy_cqrs.domain.PolicyStatus;
import com.abhishek.insurance_policy_cqrs.dto.CreatePolicyRequest;
import com.abhishek.insurance_policy_cqrs.dto.PolicyDto;
import com.abhishek.insurance_policy_cqrs.dto.UpdatePolicyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommandService commandService;

    @MockitoBean
    private QueryService queryService;

    @Test
    void list_ShouldReturnAllPolicies() throws Exception {
        // given
        PolicyDto policy1 = new PolicyDto(1, "Policy 1", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), null, null);
        PolicyDto policy2 = new PolicyDto(2, "Policy 2", PolicyStatus.INACTIVE, LocalDate.now(), LocalDate.now().plusYears(2), null, null);

        when(queryService.listPolicies()).thenReturn(Arrays.asList(policy1, policy2));

        // when/then
        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Policy 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Policy 2"));
    }

    @Test
    void get_ExistingPolicy_ShouldReturnPolicy() throws Exception {
        // given
        PolicyDto policy = new PolicyDto(1, "Test Policy", PolicyStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusYears(1), null, null);
        when(queryService.getById(1)).thenReturn(policy);

        // when/then
        mockMvc.perform(get("/api/policies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Policy"));
    }

    @Test
    void get_NonExistentPolicy_ShouldReturn404() throws Exception {
        // given
        when(queryService.getById(999)).thenReturn(null);

        // when/then
        mockMvc.perform(get("/api/policies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_ValidRequest_ShouldReturnCreatedPolicy() throws Exception {
        // given
        CreatePolicyRequest req = new CreatePolicyRequest();
        req.setName("New Policy");
        req.setStatus(PolicyStatus.ACTIVE);
        req.setCoverageStartDate(LocalDate.now());
        req.setCoverageEndDate(LocalDate.now().plusYears(1));

        InsurancePolicy created = new InsurancePolicy(req.getName(), req.getStatus(), req.getCoverageStartDate(), req.getCoverageEndDate());
        created.setId(1);

        PolicyDto dto = new PolicyDto(1, req.getName(), req.getStatus(), req.getCoverageStartDate(), req.getCoverageEndDate(), null, null);

        when(commandService.createPolicy(any(CreatePolicyRequest.class))).thenReturn(created);
        when(queryService.getById(1)).thenReturn(dto);

        // when/then
        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Policy"));
    }

    @Test
    void update_ValidRequest_ShouldReturnUpdatedPolicy() throws Exception {
        // given
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        req.setName("Updated Policy");
        req.setStatus(PolicyStatus.INACTIVE);
        req.setCoverageStartDate(LocalDate.now());
        req.setCoverageEndDate(LocalDate.now().plusYears(1));

        InsurancePolicy updated = new InsurancePolicy(req.getName(), req.getStatus(), req.getCoverageStartDate(), req.getCoverageEndDate());
        updated.setId(1);

        PolicyDto dto = new PolicyDto(1, req.getName(), req.getStatus(), req.getCoverageStartDate(), req.getCoverageEndDate(), null, null);

        when(commandService.updatePolicy(eq(1), any(UpdatePolicyRequest.class))).thenReturn(updated);
        when(queryService.getById(1)).thenReturn(dto);

        // when/then
        mockMvc.perform(put("/api/policies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Policy"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void update_NonExistentPolicy_ShouldReturn404() throws Exception {
        // given
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        req.setName("Updated Policy");
        req.setStatus(PolicyStatus.ACTIVE);
        req.setCoverageStartDate(LocalDate.now());
        req.setCoverageEndDate(LocalDate.now().plusYears(1));

        when(commandService.updatePolicy(eq(999), any(UpdatePolicyRequest.class)))
                .thenThrow(new IllegalArgumentException("Policy not found"));

        // when/then
        mockMvc.perform(put("/api/policies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }
}