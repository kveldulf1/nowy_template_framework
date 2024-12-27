import org.junit.jupiter.api.BeforeAll;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import static io.restassured.RestAssured.*;
import config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class ApiBaseTest {
    private static final Logger log = new LoggerContext().getLogger(ApiBaseTest.class);
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


        // 3. Filter for logging
        filters(new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL));

                
        log.info("API test setup completed");
    }
}
