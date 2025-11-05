package com.example.client;

import com.example.dto.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "paypal")
public interface PayPalSubscriptionsClient {

    // Plans endpoints
    @POST
    @Path("/v1/billing/plans")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PlanResponse createPlan(@HeaderParam("Authorization") String bearer,
                           @HeaderParam("PayPal-Request-Id") String requestId,
                           CreatePlanRequest request);

    @GET
    @Path("/v1/billing/plans")
    @Produces(MediaType.APPLICATION_JSON)
    ListPlansResponse listPlans(@HeaderParam("Authorization") String bearer,
                               @QueryParam("product_id") String productId,
                               @QueryParam("page_size") Integer pageSize,
                               @QueryParam("page") Integer page,
                               @QueryParam("total_required") Boolean totalRequired);

    @GET
    @Path("/v1/billing/plans/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    PlanResponse getPlan(@HeaderParam("Authorization") String bearer,
                        @PathParam("id") String id);

    @PATCH
    @Path("/v1/billing/plans/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    PlanResponse updatePlan(@HeaderParam("Authorization") String bearer,
                           @PathParam("id") String id,
                           UpdateSubscriptionRequest request);

    @POST
    @Path("/v1/billing/plans/{id}/activate")
    @Consumes(MediaType.APPLICATION_JSON)
    void activatePlan(@HeaderParam("Authorization") String bearer,
                     @PathParam("id") String id);

    @POST
    @Path("/v1/billing/plans/{id}/deactivate")
    @Consumes(MediaType.APPLICATION_JSON)
    void deactivatePlan(@HeaderParam("Authorization") String bearer,
                       @PathParam("id") String id);

    @POST
    @Path("/v1/billing/plans/{id}/update-pricing-schemes")
    @Consumes(MediaType.APPLICATION_JSON)
    void updatePricing(@HeaderParam("Authorization") String bearer,
                      @PathParam("id") String id,
                      UpdatePricingRequest request);

    // Subscriptions endpoints
    @POST
    @Path("/v1/billing/subscriptions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PayPalResponse createSubscription(@HeaderParam("Authorization") String bearer,
                                     @HeaderParam("PayPal-Request-Id") String requestId,
                                     @HeaderParam("PayPal-Client-Metadata-Id") String clientMetadataId,
                                     CreateSubscriptionRequest request);

    @GET
    @Path("/v1/billing/subscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    ListSubscriptionsResponse listSubscriptions(@HeaderParam("Authorization") String bearer,
                                               @QueryParam("plan_ids") String planIds,
                                               @QueryParam("statuses") String statuses,
                                               @QueryParam("created_after") String createdAfter,
                                               @QueryParam("created_before") String createdBefore,
                                               @QueryParam("status_updated_before") String statusUpdatedBefore,
                                               @QueryParam("status_updated_after") String statusUpdatedAfter,
                                               @QueryParam("filter") String filter,
                                               @QueryParam("page_size") Integer pageSize,
                                               @QueryParam("page") Integer page,
                                               @QueryParam("customer_ids") String customerIds);

    @GET
    @Path("/v1/billing/subscriptions/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    SubscriptionResponse getSubscription(@HeaderParam("Authorization") String bearer,
                                        @PathParam("id") String id,
                                        @QueryParam("fields") String fields);

    @PATCH
    @Path("/v1/billing/subscriptions/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateSubscription(@HeaderParam("Authorization") String bearer,
                           @PathParam("id") String id,
                           UpdateSubscriptionRequest request);

    @POST
    @Path("/v1/billing/subscriptions/{id}/revise")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PayPalResponse reviseSubscription(@HeaderParam("Authorization") String bearer,
                                     @PathParam("id") String id,
                                     ReviseSubscriptionRequest request);

    @POST
    @Path("/v1/billing/subscriptions/{id}/suspend")
    @Consumes(MediaType.APPLICATION_JSON)
    void suspendSubscription(@HeaderParam("Authorization") String bearer,
                            @PathParam("id") String id,
                            SubscriptionActionRequest request);

    @POST
    @Path("/v1/billing/subscriptions/{id}/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    void cancelSubscription(@HeaderParam("Authorization") String bearer,
                           @PathParam("id") String id,
                           SubscriptionActionRequest request);

    @POST
    @Path("/v1/billing/subscriptions/{id}/activate")
    @Consumes(MediaType.APPLICATION_JSON)
    void activateSubscription(@HeaderParam("Authorization") String bearer,
                             @PathParam("id") String id,
                             SubscriptionActionRequest request);

    @POST
    @Path("/v1/billing/subscriptions/{id}/capture")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PayPalResponse capturePayment(@HeaderParam("Authorization") String bearer,
                                 @HeaderParam("PayPal-Request-Id") String requestId,
                                 @PathParam("id") String id,
                                 CaptureSubscriptionRequest request);

    @GET
    @Path("/v1/billing/subscriptions/{id}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    SubscriptionTransactionsResponse getTransactions(@HeaderParam("Authorization") String bearer,
                                                    @PathParam("id") String id,
                                                    @QueryParam("start_time") String startTime,
                                                    @QueryParam("end_time") String endTime);
}
