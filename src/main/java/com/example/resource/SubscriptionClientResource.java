package com.example.resource;

import com.example.dto.ApiResponse;
import com.example.dto.CaptureSubscriptionRequest;
import com.example.dto.CreatePlanRequest;
import com.example.dto.CreateSubscriptionRequest;
import com.example.dto.ListPlansResponse;
import com.example.dto.ListSubscriptionsResponse;
import com.example.dto.PayPalResponse;
import com.example.dto.PlanResponse;
import com.example.dto.ReviseSubscriptionRequest;
import com.example.dto.SubscriptionActionRequest;
import com.example.dto.SubscriptionResponse;
import com.example.dto.SubscriptionTransactionsResponse;
import com.example.dto.UpdatePricingRequest;
import com.example.dto.UpdateSubscriptionRequest;
import com.example.service.SubscriptionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/billing")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubscriptionClientResource {

    @Inject
    SubscriptionService subscriptionService;

    @POST
    @Path("/subscriptions")
    public ApiResponse<PayPalResponse> createSubscription(
            @HeaderParam("X-Crochet-Access-Token") String authHeader,
            CreateSubscriptionRequest request) {
        PayPalResponse response = subscriptionService.create(authHeader, request);
        return ApiResponse.success("Subscription created successfully", response);
    }

    // Plan Management
    @POST
    @Path("/plans")
    public ApiResponse<PlanResponse> createPlan(CreatePlanRequest request) {
        PlanResponse response = subscriptionService.createPlan(request);
        return ApiResponse.success("Plan created successfully", response);
    }

    @GET
    @Path("/plans")
    public ApiResponse<ListPlansResponse> listPlans(
            @QueryParam("product_id") String productId,
            @QueryParam("page_size") Integer pageSize,
            @QueryParam("page") Integer page,
            @QueryParam("total_required") Boolean totalRequired) {
        ListPlansResponse response = subscriptionService.listPlans(productId, pageSize, page, totalRequired);
        return ApiResponse.success("Plans retrieved successfully", response);
    }

    @GET
    @Path("/plans/{planId}")
    public ApiResponse<PlanResponse> getPlan(@PathParam("planId") String planId) {
        PlanResponse response = subscriptionService.getPlan(planId);
        return ApiResponse.success("Plan retrieved successfully", response);
    }

    @PATCH
    @Path("/plans/{planId}")
    public ApiResponse<PlanResponse> updatePlan(
            @PathParam("planId") String planId,
            UpdateSubscriptionRequest request) {
        PlanResponse response = subscriptionService.updatePlan(planId, request);
        return ApiResponse.success("Plan updated successfully", response);
    }

    @POST
    @Path("/plans/{planId}/activate")
    public ApiResponse<Void> activatePlan(@PathParam("planId") String planId) {
        subscriptionService.activatePlan(planId);
        return ApiResponse.success("Plan activated successfully");
    }

    @POST
    @Path("/plans/{planId}/deactivate")
    public ApiResponse<Void> deactivatePlan(@PathParam("planId") String planId) {
        subscriptionService.deactivatePlan(planId);
        return ApiResponse.success("Plan deactivated successfully");
    }

    @PATCH
    @Path("/plans/{planId}/pricing")
    public ApiResponse<Void> updatePricing(
            @PathParam("planId") String planId,
            UpdatePricingRequest request) {
        subscriptionService.updatePricing(planId, request);
        return ApiResponse.success("Pricing updated successfully");
    }

    // Subscription Management
    @GET
    @Path("/subscriptions/list")
    public ApiResponse<ListSubscriptionsResponse> listSubscriptions(
            @QueryParam("plan_ids") String planIds,
            @QueryParam("statuses") String statuses,
            @QueryParam("created_after") String createdAfter,
            @QueryParam("created_before") String createdBefore,
            @QueryParam("status_updated_before") String statusUpdatedBefore,
            @QueryParam("status_updated_after") String statusUpdatedAfter,
            @QueryParam("filter") String filter,
            @QueryParam("page_size") Integer pageSize,
            @QueryParam("page") Integer page,
            @QueryParam("customer_ids") String customerIds) {
        ListSubscriptionsResponse response = subscriptionService.listSubscriptions(
                planIds, statuses, createdAfter, createdBefore, statusUpdatedBefore,
                statusUpdatedAfter, filter, pageSize, page, customerIds);
        return ApiResponse.success("Subscriptions retrieved successfully", response);
    }

    @GET
    @Path("/subscriptions/{subscriptionId}")
    public ApiResponse<SubscriptionResponse> getSubscription(
            @PathParam("subscriptionId") String subscriptionId,
            @QueryParam("fields") String fields) {
        SubscriptionResponse response = subscriptionService.getSubscription(subscriptionId, fields);
        return ApiResponse.success("Subscription retrieved successfully", response);
    }

    @PATCH
    @Path("/subscriptions/{subscriptionId}")
    public ApiResponse<Void> updateSubscription(
            @PathParam("subscriptionId") String subscriptionId,
            UpdateSubscriptionRequest request) {
        subscriptionService.updateSubscription(subscriptionId, request);
        return ApiResponse.success("Subscription updated successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/revise")
    public ApiResponse<PayPalResponse> reviseSubscription(
            @PathParam("subscriptionId") String subscriptionId,
            ReviseSubscriptionRequest request) {
        PayPalResponse response = subscriptionService.reviseSubscription(subscriptionId, request);
        return ApiResponse.success("Subscription revised successfully", response);
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/suspend")
    public ApiResponse<Void> suspendSubscription(
            @PathParam("subscriptionId") String subscriptionId,
            SubscriptionActionRequest request) {
        subscriptionService.suspendSubscription(subscriptionId, request);
        return ApiResponse.success("Subscription suspended successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/cancel")
    public ApiResponse<Void> cancelSubscription(
            @PathParam("subscriptionId") String subscriptionId,
            SubscriptionActionRequest request) {
        subscriptionService.cancelSubscription(subscriptionId, request);
        return ApiResponse.success("Subscription cancelled successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/activate")
    public ApiResponse<Void> activateSubscription(
            @PathParam("subscriptionId") String subscriptionId,
            SubscriptionActionRequest request) {
        subscriptionService.activateSubscription(subscriptionId, request);
        return ApiResponse.success("Subscription activated successfully");
    }

    @POST
    @Path("/subscriptions/{subscriptionId}/capture")
    public ApiResponse<PayPalResponse> capturePayment(
            @PathParam("subscriptionId") String subscriptionId,
            CaptureSubscriptionRequest request) {
        PayPalResponse response = subscriptionService.capturePayment(subscriptionId, request);
        return ApiResponse.success("Payment captured successfully", response);
    }

    @GET
    @Path("/subscriptions/{subscriptionId}/transactions")
    public ApiResponse<SubscriptionTransactionsResponse> getTransactions(
            @PathParam("subscriptionId") String subscriptionId,
            @QueryParam("start_time") String startTime,
            @QueryParam("end_time") String endTime) {
        SubscriptionTransactionsResponse response = subscriptionService.getTransactions(
                subscriptionId, startTime, endTime);
        return ApiResponse.success("Transactions retrieved successfully", response);
    }
}
