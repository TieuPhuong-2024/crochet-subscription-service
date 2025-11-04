package com.example.service.client;

import com.example.dto.UserUpdateRequest;
import io.smallrye.mutiny.Uni;

public interface UserClientService {
    Uni<Void> update(String apiKey, UserUpdateRequest request);
}
