package com.abhishek.insurance_policy_cqrs.cqrs;

import java.time.LocalDate;

public class PolicyEvents {

    public static class PolicyCreated {
        public Integer id; // can be null before persistence
        public String name;
        public String status;
        public LocalDate coverageStartDate;
        public LocalDate coverageEndDate;

        public PolicyCreated() {}

        public PolicyCreated(Integer id, String name, String status, LocalDate coverageStartDate, LocalDate coverageEndDate) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.coverageStartDate = coverageStartDate;
            this.coverageEndDate = coverageEndDate;
        }
    }

    public static class PolicyUpdated {
        public Integer id;
        public String name;
        public String status;
        public LocalDate coverageStartDate;
        public LocalDate coverageEndDate;

        public PolicyUpdated() {}

        public PolicyUpdated(Integer id, String name, String status, LocalDate coverageStartDate, LocalDate coverageEndDate) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.coverageStartDate = coverageStartDate;
            this.coverageEndDate = coverageEndDate;
        }
    }
}
