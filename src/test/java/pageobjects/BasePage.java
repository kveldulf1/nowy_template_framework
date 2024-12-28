package pageobjects;

import helpers.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {
    private By loadingIcon = By.cssSelector(".blockUI");
    protected final WebDriver driver;
    protected final String baseURL;
    protected final WebDriverWait wait;
    protected final Logger log;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        baseURL = new ConfigurationReader().getBaseUrl();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.log = (Logger) LoggerFactory.getLogger(this.getClass());
    }

    protected void waitForLoadingIconDisappear() {
        wait.until(ExpectedConditions.numberOfElementsToBe(loadingIcon, 0));
    }
}