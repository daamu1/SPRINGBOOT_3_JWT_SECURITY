package com.saurabh.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing different permissions in the system.
 * Each permission is associated with a unique string identifier.
 */
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");

    /**
     * The string representation of the permission.
     */
    @Getter
    private final String permission;

    /**
     * Get the permission enum from its string representation.
     *
     * @param permissionString The string representation of the permission.
     * @return The corresponding Permission enum or null if not found.
     */
    public static Permission fromString(String permissionString) {
        for (Permission permission : values()) {
            if (permission.getPermission().equals(permissionString)) {
                return permission;
            }
        }
        return null; // Permission not found
    }
}
