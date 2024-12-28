package helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.Optional;


/**
 * UserTestData class provides methods to retrieve test user data from the JSON file.
 * It supports both API tests and UI tests.
 */
public class UserTestData {
    private static final JsonObject usersData = TestDataReader.getTestData("users");
    
    public static JsonObject getDynamicUser() {
        return usersData.getAsJsonArray("dynamicUserData")
            .get(0)
            .getAsJsonObject();
    }
    
    public static JsonObject getPojoTestUser() {
        return usersData.getAsJsonObject("pojoTestUser");
    }
    
    public static JsonObject getDefaultTestUser() {
        return usersData.getAsJsonObject("defaultTestUser");
    }
    
    public static JsonObject getRandomValidUser() {
        JsonArray users = usersData.getAsJsonArray("validUsers");
        return users.get((int) (Math.random() * users.size()))
            .getAsJsonObject();
    }
    
    public static JsonObject getInvalidUser() {
        JsonArray users = usersData.getAsJsonArray("invalidUsers");
        return users.get((int) (Math.random() * users.size()))
            .getAsJsonObject();
    }
    
    public static String getEmail(JsonObject user) {
        return Optional.ofNullable(user.get("email"))
            .map(e -> TestDataReader.resolveDynamicValues(e.getAsString()))
            .orElse("");
    }
    
    public static String getPassword(JsonObject user) {
        return Optional.ofNullable(user.get("password"))
            .map(e -> e.getAsString())
            .orElse("");
    }
    
    public static String getUsername(JsonObject user) {
        return Optional.ofNullable(user.get("username"))
            .map(e -> e.getAsString())
            .orElse("");
    }
} 