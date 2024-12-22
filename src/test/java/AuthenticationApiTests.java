import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;

public class AuthenticationApiTests {

    private static RequestSpecification requestSpecification;
    private String validEmail = "damagehcmf@gmail.com";
    private String validPassword = "dupadupa123";
    private String invalidEmail = "damagehcmff@gmail.com";
    private String invalidPassword = "dupadupa1234";
    private String expectedErrorMessage = "Incorrect email or password";

    @BeforeAll
    public static void setupRequestSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("http://localhost:3000")
                .setBasePath("/api/login")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @BeforeAll
    public static void setupResponseSpecification() {
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void loginApiTestWithValidEmailAndValidPasswordTest() {
        String accessToken = given()
                .spec(requestSpecification)
                .body("{\"email\": \"" + validEmail + "\", \"password\": \"" + validPassword + "\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .path("access_token");

        Assertions.assertNotNull(accessToken, "Access token is null");
    }

    @Test
    public void loginApiTestWithInvalidEmailAndInvalidPasswordTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"" + invalidEmail + "\", \"password\": \"" + invalidPassword + "\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithInvalidEmailAndValidPasswordTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"" + invalidEmail + "\", \"password\": \"" + validPassword + "\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithValidEmailAndInvalidPasswordTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"" + validEmail + "\", \"password\": \"" + invalidPassword + "\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithEmptyCredentialsTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"\", \"password\": \"\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithMissingFieldsTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"" + validEmail + "\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithSpecialCharactersTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"test@#$%^&*()@test.com\", \"password\": \"pass@#$%^&*()\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithVeryLongCredentialsTest() {
        String longString = "a".repeat(257);
        String responseBody = given()
                .spec(requestSpecification)
                .body("{\"email\": \"" + longString + "@test.com\", \"password\": \"" + longString + "\"}")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithWrongContentTypeTest() {
        String responseBody = given()
                .spec(requestSpecification)
                .contentType(ContentType.XML)
                .body("<login><email>" + validEmail + "</email><password>" + validPassword + "</password></login>")
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(401)
                .extract()
                .asString();

        Assertions.assertTrue(responseBody.contains(expectedErrorMessage), "Response body is not as expected");
    }
}