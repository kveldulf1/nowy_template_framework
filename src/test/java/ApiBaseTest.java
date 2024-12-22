import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.*;
import config.RestAssuredConfig;

public class ApiBaseTest {
    private static final Logger log = LoggerFactory.getLogger(ApiBaseTest.class);
    protected static final String BASE_URI = "http://localhost:3000/api";

    @BeforeAll
    public static void setup() {
        log.info("Starting API test setup");
        // 1. Database cleanup
        try {
            given()
                    .spec(RestAssuredConfig.getRequestSpec())
                    .when()
                    .get("/restoreDB")
                    .then()
                    .statusCode(201);
        } catch (Exception e) {
            log.error("Failed to clean database", e);
            throw new RuntimeException("Test setup failed - couldn't clean database", e);
        }

        // 2. Configure RestAssured
        RestAssuredConfig.setup();
        requestSpecification = RestAssuredConfig.getRequestSpec();
        responseSpecification = RestAssuredConfig.getResponseSpec();

        log.info("API test setup completed");
    }
}
