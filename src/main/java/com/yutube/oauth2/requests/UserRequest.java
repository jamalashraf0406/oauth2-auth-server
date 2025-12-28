package com.yutube.oauth2.requests;

import com.yutube.oauth2.enums.RoleName;

public record UserRequest(
        String username,
        String password,
        boolean enabled,
        RoleName roleName) {}