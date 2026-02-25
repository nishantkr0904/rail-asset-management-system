package com.rail.asset.security;

public enum UserRole {
    ADMIN,
    MANAGER,
    VIEWER;

    public String asAuthority() {
        return "ROLE_" + name();
    }
}
