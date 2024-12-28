package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class BrowserFactory {
    private static final Logger log = (Logger) LoggerFactory.getLogger(BrowserFactory.class);

    public WebDriver createInstance(ConfigurationReader config) throws NoSuchBrowserException {
        String browserType = config.getBrowser();
        log.info("Creating {} browser instance", browserType);

        try {
            WebDriver driver = switch (browserType) {
                case "firefox" -> createFirefoxInstance(config);
                case "chrome" -> createChromeInstance(config);
                case "edge" -> createEdgeInstance(config);
                default -> throw new NoSuchBrowserException(browserType);
            };

            log.debug("Browser {} created successfully", browserType);
            return driver;

        } catch (Exception e) {
            log.error("Failed to create browser: {}", e.getMessage());
            throw new NoSuchBrowserException(e.getMessage());
        }
    }

    private WebDriver createChromeInstance(ConfigurationReader configuration) {
        ChromeOptions options = new ChromeOptions();
        log.info("Creating Chrome browser with headless={}", configuration.isHeadless());

        if (configuration.isHeadless()) {
            String[] args = { "--headless=new", "--disable-gpu", "--disable-search-engine-choice-screen" };
            log.info("Chrome arguments: {}", String.join(", ", args));
            options.addArguments(args);
        } else {
            String[] args = { "--start-maximized", "--disable-search-engine-choice-screen"};
            log.info("Chrome arguments: {}", String.join(", ", args));
            options.addArguments(args);
        }
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxInstance(ConfigurationReader configuration) {
        if (configuration.isHeadless()) {
            return new FirefoxDriver(new FirefoxOptions().addArguments("-headless"));
        } else {
            return new FirefoxDriver();
        }
    }

    private WebDriver createEdgeInstance(ConfigurationReader configuration) {
        if (configuration.isHeadless()) {
            return new EdgeDriver(new EdgeOptions().addArguments("--headless=new"));
        } else {
            return new EdgeDriver();
        }
    }
}