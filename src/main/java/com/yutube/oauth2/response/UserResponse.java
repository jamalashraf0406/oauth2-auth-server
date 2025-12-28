package com.yutube.oauth2.response;

import com.yutube.oauth2.enums.RoleName;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String username,
        boolean enabled) {}
