import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.*;

import constants.ApiEndpoints;
import io.restassured.builder.*;
import io.restassured.response.Response;

public class UsersApiTests extends ApiBaseTest {
    private static final String USER_EMAIL = "damagehcmf@gmail.com";
    private static final String USER_PASSWORD = "dupadupa123";
    private static final String TEST_FIRSTNAME = "d";
    
    private static int userId;
    private static String accessToken;

    @Test
    void createAndVerifyUserTest() {
        // Create user
        String createUserBody = String.format("""
            {
              "email": "%s",
              "firstname": "%s",
              "lastname": "d",
              "password": "%s",
              "avatar": "d"
            }
            """, USER_EMAIL, TEST_FIRSTNAME, USER_PASSWORD);

        Response createResponse = given()
            .spec(requestSpecification)
            .basePath(ApiEndpoints.CREATE_USER)
            .body(createUserBody)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .response();

        userId = createResponse.path("id");

        // Now login after user is created and get access token
        String loginBody = String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, USER_EMAIL, USER_PASSWORD);

        accessToken = given()
            .spec(requestSpecification)
            .basePath(ApiEndpoints.LOGIN)
            .body(loginBody)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .path("access_token");

        // Setup specifications after login
        requestSpecification = new RequestSpecBuilder()
            .addRequestSpecification(requestSpecification)
            .addHeader("Authorization", "Bearer " + accessToken)
            .build();

        // Verify user
        String firstname = given()
            .spec(requestSpecification)
            .when()
            .get(ApiEndpoints.GET_USER_BY_ID, userId)
            .then()
            .spec(responseSpecification)
            .statusCode(200)
            .extract()
            .path("firstname");

        Assertions.assertEquals(TEST_FIRSTNAME, firstname,
            "Created user firstname does not match expected value");
    }

    @AfterAll
    static void cleanupUser() {
        if (userId != 0 && accessToken != null) {
            given()
                .spec(requestSpecification)
                .when()
                .delete(ApiEndpoints.DELETE_USER, userId)
                .then()
                .statusCode(200);
        }
    }
}
