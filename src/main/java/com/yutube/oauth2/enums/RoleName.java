package com.yutube.oauth2.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleName {
    ROLE_ADMIN,
    ROLE_USER;

    @JsonCreator
    public static RoleName fromString(String roleName) {
        for (RoleName name: RoleName.values()) {
            if (name.name().equalsIgnoreCase(roleName)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Provided invalid Role name");
    }
}
