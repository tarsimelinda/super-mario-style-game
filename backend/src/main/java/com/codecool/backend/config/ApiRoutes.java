package com.codecool.backend.config;

public final class ApiRoutes {

    private ApiRoutes() {
    }

    public static final String API = "/api";

    public static final String USERS = API + "/users";
    public static final String PLAYERS = API + "/players";
    public static final String REGISTRATIONS = API + "/registrations";
    public static final String CHARACTERS = API + "/characters";
    public static final String LEVELS = API + "/levels";

    public static final String ADMIN = API + "/admin";
    public static final String ADMIN_CHARACTERS = ADMIN + "/characters";
    public static final String ADMIN_ENEMIES = ADMIN + "/enemies";

    public static final String BY_ID = "/{id}";
    public static final String RANDOM = "/random";
    public static final String USER_CHECKPOINT = "/{id}/checkpoint";

    public static final String API_PATTERN = API + "/*";
    public static final String API_ALL_PATTERN = API + "/**";
}