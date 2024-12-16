package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {
    private WebDriver driver;

    public WebDriver createInstance(ConfigurationReader configuration) throws NoSuchBrowserException {
        String browser = configuration.getBrowser();

        switch (browser) {
            case "firefox" -> {
                driver = createFirefoxInstance(configuration);
                return driver;
            }
            case "chrome" -> {
                driver = createChromeInstance(configuration);
                return driver;
            }
            case "edge" -> {
                driver = createEdgeInstance(configuration);
                return driver;
            }
            default -> throw new NoSuchBrowserException(browser);
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