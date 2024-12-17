package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomePage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(WelcomePage.class);
    private static final String EXPECTED_URL = "http://localhost:3000/welcome";
    
    public WelcomePage(WebDriver driver) {
        super(driver);
        log.debug("WelcomePage initialized");
    }
    
    public boolean isUrlCorrect() {
        boolean result = driver.getCurrentUrl().equals(EXPECTED_URL);
        log.debug("URL check: expected '{}', actual '{}', match: {}", 
            EXPECTED_URL, driver.getCurrentUrl(), result);
        return result;
    }
}