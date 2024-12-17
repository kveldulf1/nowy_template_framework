import helpers.BrowserFactory;
import helpers.ConfigurationReader;
import helpers.NoSuchBrowserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
    protected WebDriver driver;
    private static ConfigurationReader configuration;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeAll
    public static void loadConfiguration() {
        log.info("Loading test configuration");
        configuration = new ConfigurationReader();
    }

    @BeforeEach
    public void setup() {
        log.info("Starting browser setup");
        BrowserFactory browser = new BrowserFactory();
        try {
            driver = browser.createInstance(configuration);
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
}
