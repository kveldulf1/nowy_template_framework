import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApiBaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeAll
    public static void cleanDatabase() {
        log.info("Cleaning database");
        given().when().get("http://localhost:3000/api/restoreDB").then().statusCode(201);
    }

    @BeforeAll
    static void apiSetup() {
        baseURI = "http://localhost:3000";
        log.info("Setting up API base URI: " + baseURI);
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        log.info("Setting up API filters" + filters());
    }
}
