package com.example.client;

import com.example.dto.CreateSubscriptionRequest;
import com.example.dto.PayPalResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "paypal")
@Path("/v1/billing/subscriptions")
public interface PayPalSubscriptionsClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PayPalResponse create(@HeaderParam("Authorization") String bearer,
                          CreateSubscriptionRequest request);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    PayPalResponse getSubscription(@HeaderParam("Authorization") String bearer,
                                   @PathParam("id") String id);
}
