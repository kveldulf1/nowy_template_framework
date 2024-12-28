package utils;

import static io.restassured.RestAssured.given;
import constants.ApiEndpoints;
import helpers.TestDataReader;
import pageobjects.WelcomePage;
import pojo.authentication.LoginRequest;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import config.RestAssuredConfig;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import ch.qos.logback.classic.Logger;
import helpers.LoggerManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for common API operations like user creation, login, and deletion
 */
public class CommonApiCalls {
    private static final Logger logger = LoggerManager.getLogger(CommonApiCalls.class);
    private static String accessToken;
    private CreateUserRequest currentUser;

    /**
     * Creates a new user via API
     * Replaces timestamp placeholder in email if present
     * @return ID of the created user
     */
    public int createUser() {
        currentUser = TestDataReader.getTestData("users", CreateUserRequest.class);
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        String email = currentUser.getEmail();
        if (email == null) {
            throw new IllegalStateException("Email cannot be null in test data");
        }
        
        CreateUserRequest createUserRequest = new CreateUserRequest(
                email.replace("${timestamp}", timestamp),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getPassword(),
                currentUser.getAvatar());
        
        // Update currentUser with the actual email address used in the request
        currentUser = createUserRequest;

        CreateUserResponse response = given()
                .spec(RestAssuredConfig.getRequestSpec())
                .body(createUserRequest)
                .when()
                .post(ApiEndpoints.CREATE_USER)
                .then()
                .statusCode(201)
                .extract()
                .as(CreateUserResponse.class);

        logger.info("Created user with email: {}", createUserRequest.getEmail());
        return response.getId().intValue();
    }

    /**
     * Logs in a user and retrieves their access token
     * Updates RestAssured config with the new token
     * @param userId ID of the user to log in
     * @return Access token for the logged in user
     */
    public String logInAndGetAccessTokenForUser(int userId) {
        LoginRequest loginData = new LoginRequest(currentUser.getEmail(), currentUser.getPassword());

        accessToken = given()
                .spec(RestAssuredConfig.getRequestSpec())
                .body(loginData)
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        RestAssuredConfig.updateRequestSpecWithToken(accessToken);
        logger.info("Successfully logged in user with ID: {}", userId);
        return accessToken;
    }

    /**
     * Deletes a user if both userId and accessToken are valid
     * @param userId ID of the user to delete
     */
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

    /**
     * Sets authentication cookies in the browser for a user
     * Creates auth token if not present
     * @param driver WebDriver instance
     * @param userId ID of the user to set cookies for
     */
    private void setAuthCookies(WebDriver driver, int userId) {
        // First ensure we have a valid token
        if (accessToken == null) {
            logInAndGetAccessTokenForUser(userId);
        } 
        record CookieData(String name, String value) {
        }

        var cookies = new CookieData[] {
                new CookieData("avatar", currentUser.getAvatar()),
                new CookieData("email", currentUser.getEmail()),
                new CookieData("expires", String.valueOf(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())),
                new CookieData("firstname", currentUser.getFirstname()),
                new CookieData("id", String.valueOf(userId)),
                new CookieData("pma_lang", "en"),
                new CookieData("token", accessToken),
                new CookieData("username", currentUser.getEmail()),
                new CookieData("versionStatus", "1")
        };

        for (var cookie : cookies) {
            driver.manage().addCookie(new Cookie(cookie.name, cookie.value));
        }

        logger.info("Set authentication cookies for user ID: {}", userId);
    }

    /**
     * Creates a new user, navigates to welcome page, and logs them in via cookies
     * @param driver WebDriver instance
     * @return WelcomePage instance after successful login
     */
    public WelcomePage goToWelcomePageAsLoggedInUser(WebDriver driver) {
        int userId = createUser();
        new WelcomePage(driver).go();
        setAuthCookies(driver, userId);
        driver.navigate().refresh();
        logger.info("Navigated to welcome page as logged in user with ID: {}", userId);
        return new WelcomePage(driver);
    }
}
