package com.example.service.client;

import com.example.client.UserClient;
import com.example.dto.UserUpdateRequest;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserClientServiceImpl implements UserClientService {

    @RestClient
    @Inject
    UserClient client;

    @Override
    public Uni<Void> update(String apiKey, UserUpdateRequest request) {
        return client.update(apiKey, request);
    }
}
