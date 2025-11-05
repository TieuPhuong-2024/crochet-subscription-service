package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DTO for listing PayPal subscriptions response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ListSubscriptionsResponse {

    private final List<SubscriptionResponse> subscriptions;
    private final List<PlanResponse.Link> links;

    public ListSubscriptionsResponse(List<SubscriptionResponse> subscriptions, List<PlanResponse.Link> links) {
        this.subscriptions = subscriptions;
        this.links = links;
    }

    public List<SubscriptionResponse> getSubscriptions() { return subscriptions; }
    public List<PlanResponse.Link> getLinks() { return links; }
}
