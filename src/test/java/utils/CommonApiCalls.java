package utils;

import static io.restassured.RestAssured.given;
import constants.ApiEndpoints;
import pageobjects.WelcomePage;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import config.RestAssuredConfig;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
    private static String accessToken;
    private int userId;

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

        logger.info("Created user credentials - Email: {}, Password: {}", 
                USER_EMAIL, USER_PASSWORD);
        logger.info("Created user with ID: {}", response.getId());             
        return response.getId().intValue();
    }

    public String logInAndGetAccessTokenForUser(int userId) {
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

    public void deleteUser(int userId) {
        if (userId != 0 && accessToken != null) {
            given()
                    .spec(RestAssuredConfig.getRequestSpec())
                    .when()
                    .delete(ApiEndpoints.DELETE_USER, userId)
                    .then()
                    .statusCode(200);

            logger.info("Deleted user with ID: {}", userId);
        }
    }

    public void setAuthCookies(WebDriver driver, int userId) {
        // First ensure we have a valid token
        if (accessToken == null) {
            logInAndGetAccessTokenForUser(userId);
        }

        // Calculate expiry time (1 hour from now)
        long expiryTime = Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli();

        driver.manage().addCookie(new Cookie("avatar", TEST_NAME));
        driver.manage().addCookie(new Cookie("email", USER_EMAIL));
        driver.manage().addCookie(new Cookie("expires", String.valueOf(expiryTime)));
        driver.manage().addCookie(new Cookie("firstname", TEST_NAME));
        driver.manage().addCookie(new Cookie("id", String.valueOf(userId)));
        driver.manage().addCookie(new Cookie("pma_lang", "en"));
        driver.manage().addCookie(new Cookie("token", accessToken));
        driver.manage().addCookie(new Cookie("username", USER_EMAIL));
        driver.manage().addCookie(new Cookie("versionStatus", "1"));

        logger.info("Set authentication cookies for user ID: {}", userId);
    }

    public WelcomePage goToWelcomePageAsLoggedInUser(WebDriver driver) {
        int userId = createUser();
        new WelcomePage(driver).go();
        setAuthCookies(driver, userId);
        driver.navigate().refresh();
        return new WelcomePage(driver);
    }
}
