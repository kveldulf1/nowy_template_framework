import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.RestAssured.responseSpecification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class UsersApiTests {

    private final String validEmail = "user@example.com";
    private final String validFirstname = "John";
    private final String validLastname = "Doe";
    private final String validPassword = "Password123!";
    private final String validAvatar = "https://example.com/avatar.jpg";

    private int userId;

    @BeforeAll
    public static void setupRequestSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri("http://localhost:3000")
                .setBasePath("/api/users")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    @BeforeAll
    public static void setupResponseSpecification() {
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createUserApiTest() {

        String testFirstname = "d";

        String requestBody = """
                {
                  "email": "damagehcmf@gmail.co12232we231m",
                  "firstname": "d",
                  "lastname": "d",
                  "password": "d",
                  "avatar": "d"
                }
                """;

        int userId = given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .extract()
                .path("id");

        String firstname = given().spec(requestSpecification)
                .when()
                .get("/{id}", userId)
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .path("firstname");

        Assertions.assertEquals(testFirstname, firstname, "Compared firstname does not match, user was not created");

    }

}
