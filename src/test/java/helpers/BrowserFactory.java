package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserFactory {
    private static final Logger log = LoggerFactory.getLogger(BrowserFactory.class);

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

        if (configuration.isHeadless()) {
            options.addArguments("--headless=new", "--disable-gpu", "--disable-search-engine-choice-screen");
        } else {
            options.addArguments("--start-maximized", "--disable-search-engine-choice-screen");
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