import org.junit.jupiter.api.Test;
import constants.ApiEndpoints;
import pojo.users.CreateUserRequest;
import pojo.users.CreateUserResponse;
import com.google.gson.JsonObject;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import helpers.UserTestData;

public class PojoTests extends ApiBaseTest {

    private final JsonObject pojoTestUser = UserTestData.getPojoTestUser();

    @Test
    void createAndVerifyUserTest() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String email = pojoTestUser.get("email").getAsString()
            .replace("${timestamp}", timestamp);
        String name = pojoTestUser.get("name").getAsString();
        
        CreateUserRequest createUserRequest = new CreateUserRequest(
            email,
            name,
            name,
            pojoTestUser.get("password").getAsString(),
            name
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
