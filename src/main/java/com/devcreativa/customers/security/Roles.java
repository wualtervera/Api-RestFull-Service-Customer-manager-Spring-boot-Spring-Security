package com.devcreativa.customers.security;

import java.util.Arrays;
import java.util.List;

public class Roles {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String MODERATOR = "ROLE_MODERATOR";
    public static final String USER = "ROLE_USER";

    private Roles() {
    }

    public static List<String> getAdminAccess() {
        return Arrays.asList(ADMIN, MODERATOR, USER);
    }

    public static List<String> getModeratorAccess() {
        return Arrays.asList(MODERATOR, USER);
    }

    public static List<String> getUserAccess() {
        return List.of(USER);
    }

}
