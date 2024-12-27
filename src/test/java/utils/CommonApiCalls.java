package utils;

import static io.restassured.RestAssured.given;
import constants.ApiEndpoints;
import models.LoginData;
import pageobjects.WelcomePage;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import config.RestAssuredConfig;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import utils.TestDataManager;

/**
 * Utility class for common API operations
 */
public class CommonApiCalls {
    private static final Logger logger = LoggerFactory.getLogger(CommonApiCalls.class);
    private static String accessToken;
    private int userId;
    private CreateUserRequest currentUser;

    public int createUser() {
        // Pobierz dane nowego użytkownika z JSON
        currentUser = TestDataManager.getTestData("users", CreateUserRequest.class);
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        CreateUserRequest createUserRequest = new CreateUserRequest(
                currentUser.getEmail().replace("${timestamp}", timestamp),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getPassword(),
                currentUser.getAvatar());
        
        // Aktualizujemy currentUser o rzeczywisty email
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

    public String logInAndGetAccessTokenForUser(int userId) {
        // Używamy danych utworzonego użytkownika
        LoginData loginData = new LoginData(currentUser.getEmail(), currentUser.getPassword());

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

    public WelcomePage goToWelcomePageAsLoggedInUser(WebDriver driver) {
        int userId = createUser();
        new WelcomePage(driver).go();
        setAuthCookies(driver, userId);
        driver.navigate().refresh();
        return new WelcomePage(driver);
    }
}
