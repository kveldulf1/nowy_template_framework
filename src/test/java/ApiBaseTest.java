import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApiBaseTest {

    private static final Logger log = LoggerFactory.getLogger(ApiBaseTest.class);
    protected static final String BASE_URI = "http://localhost:3000/api";

    @BeforeAll
    public static void setup() {
        log.info("Starting API test setup");

        // 1. Database cleanup
        try {
            given()
                    .when()
                    .get(BASE_URI + "/restoreDB")
                    .then()
                    .statusCode(201);
        } catch (Exception e) {
            log.error("Failed to clean database", e);
            throw new RuntimeException("Test setup failed - couldn't clean database", e);
        }

        // 2. Configure filters
        filters(new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.BODY));

        // 3. Configure request specification
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        // 4. Configure response specification
        responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();

        log.info("API test setup completed");
    }
}
