package com.example.service.client;

import com.example.dto.UserUpdateRequest;

public interface UserClientService {
    void update(String apiKey, UserUpdateRequest request);
}
