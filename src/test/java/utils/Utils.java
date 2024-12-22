package utils;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import constants.ApiEndpoints;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import config.RestAssuredConfig;

public class Utils {

    private static final String USER_EMAIL = "test_" + System.currentTimeMillis() + "@example.com";
    private static final String USER_PASSWORD = "Test123!@#";
    private static final String TEST_NAME = "TestUser";
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Utils.class);

    public Utils() {
    }

    public int createUser() {       

        CreateUserRequest createUserRequest = new CreateUserRequest(
                USER_EMAIL,
                TEST_NAME,
                TEST_NAME,
                USER_PASSWORD,
                TEST_NAME);

        CreateUserResponse response = given()
                .spec(RestAssuredConfig.getRequestSpec())
                .body(createUserRequest)
                .when()
                .post(ApiEndpoints.CREATE_USER)
                .then()
                .statusCode(201)
                .extract()
                .as(CreateUserResponse.class);

        
        logger.info("User created with ID: {}", response.getId().intValue());
        return response.getId().intValue();
    }

    // public String getAccessTokenForUser(int userId) {
    //     return "Bearer " + System.currentTimeMillis();
    // }

}
