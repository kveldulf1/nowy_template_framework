import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.*;
import constants.ApiEndpoints;
import utils.CommonApiCalls;
import com.google.gson.JsonObject;
import helpers.UserTestData;

public class UsersApiTests extends ApiBaseTest {
    private static final JsonObject dynamicUser = UserTestData.getDynamicUser();

    private static int userId;
    static CommonApiCalls commonApiCalls = new CommonApiCalls();

    @Test
    void createAndVerifyUserTest() {
        userId = commonApiCalls.createUser();
        commonApiCalls.logInAndGetAccessTokenForUser(userId);

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

        Assertions.assertEquals(dynamicUser.get("firstname").getAsString(), firstname,
                "Created user firstname does not match expected value");
    }

    @AfterAll
    static void cleanupUser() {
        commonApiCalls.deleteUser(userId);
    }
}
