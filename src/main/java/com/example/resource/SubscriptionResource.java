package com.example.resource;

import com.example.dto.ApiResponse;
import com.example.dto.UserSubscription;
import com.example.service.SubscriptionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/subscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriptionResource {

    @Inject
    SubscriptionService subscriptionService;

    @GET
    @Path("/user/{userId}")
    public ApiResponse<UserSubscription> getUserSubscription(
        @PathParam("userId") String userId
    ) {
        UserSubscription userSubscription =
            subscriptionService.getUserSubscription(userId);
        return ApiResponse.success(
            "User subscription retrieved successfully",
            userSubscription
        );
    }

    @GET
    @Path("/return")
    public ApiResponse<Void> handleSubscriptionReturn(
        @QueryParam("subscription_id") String subscriptionId
    ) {
        subscriptionService.handleSubscriptionReturn(subscriptionId);
        return ApiResponse.success("Subscription activated successfully");
    }

    @POST
    @Path("/webhook")
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> handleWebhook(String payload) {
        subscriptionService.handleWebhookEvent(payload);
        return ApiResponse.success("Webhook processed successfully");
    }
}
