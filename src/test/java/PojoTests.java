import org.junit.jupiter.api.Test;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import constants.ApiEndpoints;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class PojoTests extends ApiBaseTest {

    private static final String USER_EMAIL = "test_" + System.currentTimeMillis() + "@example.com";
    private static final String USER_PASSWORD = "Test123!@#";
    private static final String TEST_NAME = "TestUser";

    @Test
    void createAndVerifyUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest(
            USER_EMAIL, 
            TEST_NAME,
            TEST_NAME,
            USER_PASSWORD,
            TEST_NAME
        );
        
        CreateUserResponse response = given()
            .body(createUserRequest)
            .when()
            .post(ApiEndpoints.CREATE_USER)
            .then()
            .statusCode(201)
            .extract()
            .as(CreateUserResponse.class);

        int userId = response.getId().intValue();

        assertNotNull(userId, "User ID should not be null");
        assertTrue(userId > 0, "User ID should be positive");
    }
}
