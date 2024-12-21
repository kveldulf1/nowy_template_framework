import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.RestAssured.responseSpecification;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pageobjects.MainPage;
import pageobjects.RegisterPage;
import pageobjects.WelcomePage;

public class UsersApiTests {

        private final static String validEmail = "user@example.com";
        private final static String validFirstname = "John";
        private final static String validLastname = "Doe";
        private final static String validPassword = "Password123!";
        private final static String validAvatar = "https://example.com/avatar.jpg";

        private static int userId;
        private static String accessToken;

        private static String requestBody = """
                        {
                            "email": "damagehcmf@gmail.com",
                            "password": "dupadupa123"
                        }
                        """;

        // @BeforeEach
        // @Order(2)
        // public void loginApiTestWithValidEmailAndValidPasswordTest() {
        // accessToken = given()
        // .log().all()
        // .baseUri("http://localhost:3000")
        // .basePath("/api/login")
        // .contentType(ContentType.JSON)
        // .accept(ContentType.JSON)
        // .body(requestBody)
        // .when()
        // .post()
        // .then()
        // .log().body()
        // .contentType(ContentType.JSON)
        // .statusCode(200)
        // .extract()
        // .path("access_token");

        // Assertions.assertNotNull(accessToken, "Access token is null");
        // }

        @BeforeEach
        public void setupSpecifications() {
                accessToken = given()
                                .log().all()
                                .baseUri("http://localhost:3000")
                                .basePath("/api/login")
                                .contentType(ContentType.JSON)
                                .accept(ContentType.JSON)
                                .body(requestBody)
                                .when()
                                .post()
                                .then()
                                .log().body()
                                .contentType(ContentType.JSON)
                                .statusCode(200)
                                .extract()
                                .path("access_token");

                requestSpecification = new RequestSpecBuilder()
                                .log(LogDetail.ALL)
                                .addHeader("Authorization", "Bearer " + accessToken)
                                .setBaseUri("http://localhost:3000")
                                .setBasePath("/api/users")
                                .setContentType(ContentType.JSON)
                                .setAccept(ContentType.JSON)
                                .build();

                responseSpecification = new ResponseSpecBuilder()
                                .log(LogDetail.ALL)
                                .expectContentType(ContentType.JSON)
                                .build();
        }

        @Test
        public void createUserApiTest() {

                given().when().get("http://localhost:3000/api/restoreDB").then().statusCode(201);

                String testFirstname = "d";

                String createUserBody = """
                                {
                                  "email": "damagehcmf@gmail.com",
                                  "firstname": "d",
                                  "lastname": "d",
                                  "password": "dupadupa123",
                                  "avatar": "d"
                                }
                                """;
                Response response = given()
                                .spec(requestSpecification)
                                .body(createUserBody)
                                .when()
                                .post()
                                .then()
                                .spec(responseSpecification)
                                .statusCode(201)
                                .extract()
                                .response();

                userId = response.path("id");
                accessToken = response.path("access_token");

                String firstname = given().spec(requestSpecification)
                                .when()
                                .get("/{id}", userId)
                                .then()
                                .spec(responseSpecification)
                                .statusCode(200)
                                .extract()
                                .path("firstname");

                Assertions.assertEquals(testFirstname, firstname,
                                "Compared firstname does not match, user was not created");

                String accessToken = given()
                                .log().all()
                                .baseUri("http://localhost:3000")
                                .basePath("/api/login")
                                .contentType(ContentType.JSON)
                                .accept(ContentType.JSON)
                                .body(String.format("""
                                                {
                                                    "email": "%s",
                                                    "password": "%s"
                                                }
                                                """, "damagehcmf@gmail.com", "dupadupa123"))
                                .when()
                                .post()
                                .then()
                                .log().body()
                                .contentType(ContentType.JSON)
                                .statusCode(200)
                                .extract()
                                .path("access_token");
                given()
                                .spec(requestSpecification)
                                .when()
                                .delete("/{id}", userId)
                                .then()
                                .spec(responseSpecification)
                                .statusCode(200);
        }

}
