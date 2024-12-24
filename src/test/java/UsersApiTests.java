import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.*;

import constants.ApiEndpoints;
import utils.CommonApiCalls;

public class UsersApiTests extends ApiBaseTest {
    private static final String TEST_FIRSTNAME = "TestUser";

    private static int userId;
    private static String accessToken;

    static CommonApiCalls commonApiCalls = new CommonApiCalls();

    @Test
    void createAndVerifyUserTest() {
        userId = commonApiCalls.createUser();
        accessToken = commonApiCalls.logInAndGetAccessTokenForUser(userId);

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
        commonApiCalls.deleteUser(userId);
    }
}
