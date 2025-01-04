import helpers.BrowserFactory;
import helpers.ConfigurationReader;
import helpers.NoSuchBrowserException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import ch.qos.logback.classic.Logger;
import static io.restassured.RestAssured.given;
import org.slf4j.LoggerFactory;


public class BaseTest {
    protected WebDriver driver;
    private static final ConfigurationReader configuration = ConfigurationReader.getInstance();
    private static final Logger log = (Logger) LoggerFactory.getLogger(BaseTest.class);

    @BeforeAll
    public static void loadConfiguration() {
        log.info("Loading test configuration");
    }

    @BeforeEach
    public void setup() {
        log.info("Starting browser setup");
        try {
            driver = new BrowserFactory().createInstance(configuration);
            log.info("Browser started successfully");
        } catch (NoSuchBrowserException e) {
            log.error("Failed to start browser", e);
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void quitDriver() {
        log.info("Closing browser");       
        driver.quit();
    }

    @BeforeAll
    public static void cleanDatabase() {
        log.info("Cleaning database");
        given().when().get("http://localhost:3000/api/restoreDB").then().statusCode(201);
    }
}
