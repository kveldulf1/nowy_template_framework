package pageobjects;

import org.openqa.selenium.WebDriver;

public class WelcomePage extends BasePage {
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

    public void go() {
        driver.get(EXPECTED_URL);
        log.debug("Navigated to {}", EXPECTED_URL);
    }
}