package utils;

import static io.restassured.RestAssured.given;
import constants.ApiEndpoints;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import config.RestAssuredConfig;

/**
 * Utility class for common API operations
 */
public class CommonApiCalls {
    // Test data for user creation
    private static final String USER_EMAIL = "test_" + System.currentTimeMillis() + "@example.com";
    private static final String USER_PASSWORD = "Test123!@#";
    private static final String TEST_NAME = "TestUser";
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CommonApiCalls.class);
    
    // Stores the access token for authenticated requests
    public static String accessToken;

    public int createUser() {
        // Create request body with test data
        CreateUserRequest createUserRequest = new CreateUserRequest(
                USER_EMAIL,
                TEST_NAME,
                TEST_NAME,
                USER_PASSWORD,
                TEST_NAME);

        // Send POST request to create user
        CreateUserResponse response = given()
                .spec(RestAssuredConfig.getRequestSpec())
                .body(createUserRequest)
                .when()
                .post(ApiEndpoints.CREATE_USER)
                .then()
                .statusCode(201)
                .extract()
                .as(CreateUserResponse.class);

        logger.info("User created with ID: {}", response.getId().intValue());
        return response.getId().intValue();
    }

    public String getAccessTokenForUser(int userId) {
        String loginBody = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, USER_EMAIL, USER_PASSWORD);

        // Send POST request to login endpoint
        accessToken = given()
                .spec(RestAssuredConfig.getRequestSpec())
                .body(loginBody)
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        // Update global request specification with the new token
        RestAssuredConfig.updateRequestSpecWithToken(accessToken);

        logger.info("Access token obtained: {}", accessToken);
        return accessToken;
    }
}
