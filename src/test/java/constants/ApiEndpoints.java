package constants;
public class ApiEndpoints {
    // Base paths
    public static final String AUTHENTICATION_PATH = "/authentication";
    public static final String USERS_PATH = "/users";
    
    // Authentication endpoints
    public static final String LOGIN = "/login";
    
    // Users endpoints
    public static final String GET_ALL_USERS = USERS_PATH;
    public static final String CREATE_USER = USERS_PATH;
    public static final String GET_USER_BY_ID = USERS_PATH + "/{id}";
    public static final String UPDATE_USER = USERS_PATH + "/{id}";
    public static final String PATCH_USER = USERS_PATH + "/{id}";
    public static final String DELETE_USER = USERS_PATH + "/{id}";
    public static final String USER_INFO = USERS_PATH + "/{id}";
} 