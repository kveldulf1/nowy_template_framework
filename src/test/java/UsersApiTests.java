import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.*;
import io.restassured.builder.*;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UsersApiTests extends ApiBaseTest {
    private static final String BASE_URI = "http://localhost:3000";
    private static final String USER_EMAIL = "damagehcmf@gmail.com";
    private static final String USER_PASSWORD = "dupadupa123";
    private static final String TEST_FIRSTNAME = "d";
    
    private static int userId;
    private static String accessToken;

    @BeforeAll
    static void setupDB() {
        // Reset database
        given()
            .when()
            .get(BASE_URI + "/api/restoreDB")
            .then()
            .statusCode(201);
    }

    @Test
    void createAndVerifyUserTest() {
        // Create user without token
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
            .log().all()
            .baseUri(BASE_URI)
            .basePath("/api/users")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(createUserBody)
            .when()
            .post()
            .then()
            .log().body()
            .statusCode(201)
            .extract()
            .response();

        userId = createResponse.path("id");

        // Now login after user is created
        String loginBody = String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, USER_EMAIL, USER_PASSWORD);

        accessToken = given()
            .log().all()
            .baseUri(BASE_URI)
            .basePath("/api/login")
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(loginBody)
            .when()
            .post()
            .then()
            .log().body()
            .statusCode(200)
            .extract()
            .path("access_token");

        // Setup specifications after login
        requestSpecification = new RequestSpecBuilder()
            .log(LogDetail.ALL)
            .addHeader("Authorization", "Bearer " + accessToken)
            .setBaseUri(BASE_URI)
            .setBasePath("/api/users")
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();

        responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectContentType(ContentType.JSON)
            .build();

        // Verify created user
        String firstname = given()
            .spec(requestSpecification)
            .when()
            .get("/{id}", userId)
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
        if (userId != 0) {
            given()
                .log().all()
                .baseUri(BASE_URI)
                .basePath("/api/users")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("/{id}", userId)
                .then()
                .statusCode(200);
        }
    }
}
