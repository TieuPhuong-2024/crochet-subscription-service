package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayPalSubscriptionResponse {

    private String status;
    private String id;
    @JsonProperty("create_time")
    private Instant createTime;
    private List<Link> links;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        private String href;
        private String rel;
        private String method;
    }
}
