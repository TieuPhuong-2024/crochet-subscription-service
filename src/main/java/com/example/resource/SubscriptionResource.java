package com.example.resource;

import com.example.dto.*;
import com.example.service.SubscriptionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/subscriptions")
public class SubscriptionResource {

    @Inject
    SubscriptionService subscriptionService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<PayPalResponse> createSubscription(@HeaderParam("X-Crochet-Access-Token") String authHeader,
                                                         CreateSubscriptionRequest request) {
        PayPalResponse response = subscriptionService.create(authHeader, request);
        return ApiResponse.success("Subscription created successfully", response);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<UserSubscription> getUserSubscription(@QueryParam("user_id") String userId) {
        UserSubscription userSubscription = subscriptionService.getUserSubscription(userId);
        return ApiResponse.success("User subscription retrieved successfully", userSubscription);
    }

    @GET
    @Path("/return")
    public ApiResponse<Void> handleSubscriptionReturn(@QueryParam("subscription_id") String subscriptionId) {
        subscriptionService.handleSubscriptionReturn(subscriptionId);
        return ApiResponse.success("Subscription activated successfully for ID: " + subscriptionId);
    }

    @GET
    @Path("/cancel")
    public ApiResponse<Void> handleSubscriptionCancel(@QueryParam("subscription_id") String subscriptionId) {
        return ApiResponse.success("Subscription cancellation acknowledged for ID: " + subscriptionId);
    }

    @POST
    @Path("/webhook")
    public ApiResponse<Void> handleWebhook(String payload) {
    subscriptionService.handleWebhookEvent(payload);
    return ApiResponse.success("Webhook processed successfully");
    }

    // Plan endpoints
    @POST
    @Path("/plans")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<PlanResponse> createPlan(CreatePlanRequest request) {
        PlanResponse response = subscriptionService.createPlan(null, request); // TODO: Add auth
        return ApiResponse.success("Plan created successfully", response);
    }

    @GET
    @Path("/plans")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<ListPlansResponse> listPlans(@QueryParam("product_id") String productId,
                                                   @QueryParam("page_size") Integer pageSize,
                                                   @QueryParam("page") Integer page,
                                                   @QueryParam("total_required") Boolean totalRequired) {
        ListPlansResponse response = subscriptionService.listPlans(null, productId, pageSize, page, totalRequired); // TODO: Add auth
        return ApiResponse.success("Plans retrieved successfully", response);
    }

    @GET
    @Path("/plans/{planId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<PlanResponse> getPlan(@PathParam("planId") String planId) {
        PlanResponse response = subscriptionService.getPlan(null, planId); // TODO: Add auth
        return ApiResponse.success("Plan retrieved successfully", response);
    }

    @PATCH
    @Path("/plans/{planId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<PlanResponse> updatePlan(@PathParam("planId") String planId, UpdateSubscriptionRequest request) {
        PlanResponse response = subscriptionService.updatePlan(null, planId, request); // TODO: Add auth
        return ApiResponse.success("Plan updated successfully", response);
    }

    @POST
    @Path("/plans/{planId}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> activatePlan(@PathParam("planId") String planId) {
        subscriptionService.activatePlan(null, planId); // TODO: Add auth
        return ApiResponse.success("Plan activated successfully");
    }

    @POST
    @Path("/plans/{planId}/deactivate")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> deactivatePlan(@PathParam("planId") String planId) {
        subscriptionService.deactivatePlan(null, planId); // TODO: Add auth
        return ApiResponse.success("Plan deactivated successfully");
    }

    @POST
    @Path("/plans/{planId}/update-pricing")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> updatePricing(@PathParam("planId") String planId, UpdatePricingRequest request) {
        subscriptionService.updatePricing(null, planId, request); // TODO: Add auth
        return ApiResponse.success("Pricing updated successfully");
    }

    // Additional subscription endpoints
    @GET
    @Path("/subscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<ListSubscriptionsResponse> listSubscriptions(@QueryParam("plan_ids") String planIds,
                                                                  @QueryParam("statuses") String statuses,
                                                                  @QueryParam("created_after") String createdAfter,
                                                                  @QueryParam("created_before") String createdBefore,
                                                                  @QueryParam("status_updated_before") String statusUpdatedBefore,
                                                                  @QueryParam("status_updated_after") String statusUpdatedAfter,
                                                                  @QueryParam("filter") String filter,
                                                                  @QueryParam("page_size") Integer pageSize,
                                                                  @QueryParam("page") Integer page,
                                                                  @QueryParam("customer_ids") String customerIds) {
        ListSubscriptionsResponse response = subscriptionService.listSubscriptions(null, planIds, statuses, createdAfter,
                createdBefore, statusUpdatedBefore, statusUpdatedAfter, filter, pageSize, page, customerIds); // TODO: Add auth
        return ApiResponse.success("Subscriptions retrieved successfully", response);
    }

    @GET
    @Path("/subscriptions/{subscriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<SubscriptionResponse> getSubscription(@PathParam("subscriptionId") String subscriptionId,
                                                            @QueryParam("fields") String fields) {
        SubscriptionResponse response = subscriptionService.getSubscription(null, subscriptionId, fields); // TODO: Add auth
        return ApiResponse.success("Subscription retrieved successfully", response);
    }

    @PATCH
    @Path("/subscriptions/{subscriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> updateSubscription(@PathParam("subscriptionId") String subscriptionId,
                                               UpdateSubscriptionRequest request) {
        subscriptionService.updateSubscription(null, subscriptionId, request); // TODO: Add auth
        return ApiResponse.success("Subscription updated successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/revise")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<PayPalResponse> reviseSubscription(@PathParam("subscriptionId") String subscriptionId,
                                                         ReviseSubscriptionRequest request) {
        PayPalResponse response = subscriptionService.reviseSubscription(null, subscriptionId, request); // TODO: Add auth
        return ApiResponse.success("Subscription revised successfully", response);
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/suspend")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> suspendSubscription(@PathParam("subscriptionId") String subscriptionId,
                                                SubscriptionActionRequest request) {
        subscriptionService.suspendSubscription(null, subscriptionId, request); // TODO: Add auth
        return ApiResponse.success("Subscription suspended successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/cancel")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> cancelSubscription(@PathParam("subscriptionId") String subscriptionId,
                                               SubscriptionActionRequest request) {
        subscriptionService.cancelSubscription(null, subscriptionId, request); // TODO: Add auth
        return ApiResponse.success("Subscription cancelled successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/activate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<Void> activateSubscription(@PathParam("subscriptionId") String subscriptionId,
                                                 SubscriptionActionRequest request) {
        subscriptionService.activateSubscription(null, subscriptionId, request); // TODO: Add auth
        return ApiResponse.success("Subscription activated successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/capture")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse<PayPalResponse> capturePayment(@PathParam("subscriptionId") String subscriptionId,
                                                     CaptureSubscriptionRequest request) {
        PayPalResponse response = subscriptionService.capturePayment(null, subscriptionId, request); // TODO: Add auth
        return ApiResponse.success("Payment captured successfully", response);
    }

    @GET
    @Path("/subscriptions/{subscriptionId}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse<SubscriptionTransactionsResponse> getTransactions(@PathParam("subscriptionId") String subscriptionId,
                                                                        @QueryParam("start_time") String startTime,
                                                                        @QueryParam("end_time") String endTime) {
        SubscriptionTransactionsResponse response = subscriptionService.getTransactions(null, subscriptionId, startTime, endTime); // TODO: Add auth
        return ApiResponse.success("Transactions retrieved successfully", response);
    }
}
