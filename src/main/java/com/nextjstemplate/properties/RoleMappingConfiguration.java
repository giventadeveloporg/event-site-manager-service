package com.nextjstemplate.properties;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for mapping Clerk organization roles to application roles.
 */
@Configuration
public class RoleMappingConfiguration {

    /**
     * Role hierarchy levels (higher number = more privileges).
     */
    public enum RoleHierarchy {
        GUEST(10),
        USER(20),
        MANAGER(30),
        ADMIN(40),
        SUPER_ADMIN(50);

        private final int level;

        RoleHierarchy(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    /**
     * Create the role mapping bean.
     *
     * Maps Clerk organization roles to application roles.
     */
    @Bean
    public Map<String, String> clerkToApplicationRoleMapping() {
        Map<String, String> mapping = new HashMap<>();

        // Clerk Organization Roles (from Clerk Dashboard)
        mapping.put("org:admin", "ADMIN");
        mapping.put("org:member", "USER");
        mapping.put("org:billing", "MANAGER");
        mapping.put("org:guest", "GUEST");

        // Simple role names (fallback)
        mapping.put("admin", "ADMIN");
        mapping.put("member", "USER");
        mapping.put("user", "USER");
        mapping.put("billing", "MANAGER");
        mapping.put("manager", "MANAGER");
        mapping.put("guest", "GUEST");

        // Custom roles (can be extended)
        mapping.put("org:super_admin", "SUPER_ADMIN");
        mapping.put("super_admin", "SUPER_ADMIN");

        return mapping;
    }

    /**
     * Get application role from Clerk role.
     *
     * @param clerkRole the role from Clerk
     * @return the mapped application role
     */
    public String getApplicationRole(String clerkRole) {
        if (clerkRole == null || clerkRole.isEmpty()) {
            return "USER"; // Default role
        }

        Map<String, String> mapping = clerkToApplicationRoleMapping();
        return mapping.getOrDefault(clerkRole.toLowerCase(), "USER");
    }

    /**
     * Check if a role has at least the required level.
     *
     * @param userRole     the user's role
     * @param requiredRole the required role
     * @return true if user role meets or exceeds required role
     */
    public boolean hasRole(String userRole, String requiredRole) {
        try {
            RoleHierarchy userHierarchy = RoleHierarchy.valueOf(userRole.toUpperCase());
            RoleHierarchy requiredHierarchy = RoleHierarchy.valueOf(requiredRole.toUpperCase());
            return userHierarchy.getLevel() >= requiredHierarchy.getLevel();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get all available application roles.
     *
     * @return array of all role names
     */
    public String[] getAllRoles() {
        return new String[] { "GUEST", "USER", "MANAGER", "ADMIN", "SUPER_ADMIN" };
    }

    /**
     * Get default role for new users.
     *
     * @return the default role name
     */
    public String getDefaultRole() {
        return "USER";
    }
}
