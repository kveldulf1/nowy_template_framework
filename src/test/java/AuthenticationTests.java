import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthenticationTests {

    private String validEmail = "damagehcmf@gmail.com";
    private String validPassword = "dupadupa123";
    private String invalidEmail = "damagehcmff@gmail.com";
    private String invalidPassword = "dupadupa1234";

    @Test
    public void loginApiTestWithValidEmailAndValidPasswordTest() {
        String accessToken = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"email\": \"" + validEmail + "\", \"password\": \"" + validPassword + "\"}")
                .when()
                .post("http://localhost:3000/api/login")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .path("access_token");

        Assertions.assertNotNull(accessToken, "Access token is null");
    }

    @Test
    public void loginApiTestWithInvalidEmailAndInvalidPasswordTest() {
        String responseBody = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"email\": \"" + invalidEmail + "\", \"password\": \"" + invalidPassword + "\"}")
                .when()
                .post("http://localhost:3000/api/login")
                .then()
                .log()
                .body()
                .extract().asString();

        Assertions.assertTrue(responseBody.contains("Incorrect email or password"), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithInvalidEmailAndValidPasswordTest() {
        String responseBody = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"email\": \"" + invalidEmail + "\", \"password\": \"" + validPassword + "\"}")
                .when()
                .post("http://localhost:3000/api/login")
                .then()
                .log()
                .body()
                .extract().asString();

        Assertions.assertTrue(responseBody.contains("Incorrect email or password"), "Response body is not as expected");
    }

    @Test
    public void loginApiTestWithValidEmailAndInvalidPasswordTest() {
        String responseBody = given()
                .log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"email\": \"" + validEmail + "\", \"password\": \"" + invalidPassword + "\"}")
                .when()
                .post("http://localhost:3000/api/login")
                .then()
                .log()
                .body()
                .extract().asString();

        Assertions.assertTrue(responseBody.contains("Incorrect email or password"), "Response body is not as expected");
    }
}
