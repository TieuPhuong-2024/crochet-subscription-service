package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DTO for listing PayPal plans response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ListPlansResponse {

    private final List<PlanSummary> plans;
    private final List<PlanResponse.Link> links;

    public ListPlansResponse(List<PlanSummary> plans, List<PlanResponse.Link> links) {
        this.plans = plans;
        this.links = links;
    }

    public List<PlanSummary> getPlans() { return plans; }
    public List<PlanResponse.Link> getLinks() { return links; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PlanSummary {
        private final String id;
        private final String name;
        private final String status;
        private final String description;
        private final String usageType;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @com.fasterxml.jackson.annotation.JsonProperty("create_time")
        private final String createTime;

        private final List<PlanResponse.Link> links;

        public PlanSummary(String id, String name, String status, String description,
                          String usageType, String createTime, List<PlanResponse.Link> links) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.description = description;
            this.usageType = usageType;
            this.createTime = createTime;
            this.links = links;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getStatus() { return status; }
        public String getDescription() { return description; }

        @com.fasterxml.jackson.annotation.JsonProperty("usage_type")
        public String getUsageType() { return usageType; }

        @com.fasterxml.jackson.annotation.JsonProperty("create_time")
        public String getCreateTime() { return createTime; }

        public List<PlanResponse.Link> getLinks() { return links; }
    }
}
