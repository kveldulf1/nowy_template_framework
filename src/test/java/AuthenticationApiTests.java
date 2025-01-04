import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import constants.ApiEndpoints;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;

import helpers.UserTestData;
import io.qameta.allure.*;

@Epic("API Testing")
@Feature("Authentication API")
public class AuthenticationApiTests extends ApiBaseTest {

    private String expectedErrorMessage = "Incorrect email or password";
    private JsonObject validUser = UserTestData.getRandomValidUser();
    private String validEmail = UserTestData.getEmail(validUser);
    private String validPassword = UserTestData.getPassword(validUser);
    private JsonObject invalidUser = UserTestData.getInvalidUser();
    private String invalidEmail = UserTestData.getEmail(invalidUser);
    private String invalidPassword = UserTestData.getPassword(invalidUser);
    

    @Test
    @Story("Login API")
    @Description("Verify login with valid credentials returns access token")
    @Severity(SeverityLevel.BLOCKER)
    public void loginApiTestWithValidEmailAndValidPasswordTest() {
        String accessToken = given()
                .body("{\"email\": \"" + this.validEmail + "\", \"password\": \"" + this.validPassword + "\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        Assertions.assertNotNull(accessToken, "Access token is null");
    }

    @Test
    @Story("Login API Security")
    @Description("Verify login fails with invalid email and password")
    @Severity(SeverityLevel.CRITICAL)
    public void loginApiTestWithInvalidEmailAndInvalidPasswordTest() {
        String responseBody = given()
                .body("{\"email\": \"" + invalidEmail + "\", \"password\": \"" + invalidPassword + "\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Security")
    @Description("Verify login fails with invalid email and valid password")
    @Severity(SeverityLevel.CRITICAL)
    public void loginApiTestWithInvalidEmailAndValidPasswordTest() {
        String responseBody = given()
                .body("{\"email\": \"" + invalidEmail + "\", \"password\": \"" + validPassword + "\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Security")
    @Description("Verify login fails with valid email and invalid password")
    @Severity(SeverityLevel.CRITICAL)
    public void loginApiTestWithValidEmailAndInvalidPasswordTest() {
        String responseBody = given()
                .body("{\"email\": \"" + validEmail + "\", \"password\": \"" + invalidPassword + "\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Validation")
    @Description("Verify login fails with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    public void loginApiTestWithEmptyCredentialsTest() {
        String responseBody = given()
                .body("{\"email\": \"\", \"password\": \"\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Validation")
    @Description("Verify login fails with missing required fields")
    @Severity(SeverityLevel.NORMAL)
    public void loginApiTestWithMissingFieldsTest() {
        String responseBody = given()
                .body("{\"email\": \"" + validEmail + "\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Security")
    @Description("Verify login handles special characters properly")
    @Severity(SeverityLevel.MINOR)
    public void loginApiTestWithSpecialCharactersTest() {
        String responseBody = given()
                .body("{\"email\": \"test@#$%^&*()@test.com\", \"password\": \"pass@#$%^&*()\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Validation")
    @Description("Verify login handles very long credentials properly")
    @Severity(SeverityLevel.MINOR)
    public void loginApiTestWithVeryLongCredentialsTest() {
        String longString = "a".repeat(257);
        String responseBody = given()
                .body("{\"email\": \"" + longString + "@test.com\", \"password\": \"" + longString + "\"}")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    @Story("Login API Validation")
    @Description("Verify login handles wrong content type properly")
    @Severity(SeverityLevel.NORMAL)
    public void loginApiTestWithWrongContentTypeTest() {
        String responseBody = given()
                .contentType(ContentType.XML)
                .body("<login><email>" + validEmail + "</email><password>" + validPassword + "</password></login>")
                .when()
                .post(ApiEndpoints.LOGIN)
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }
}
