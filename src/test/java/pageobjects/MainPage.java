package pageobjects;

import org.openqa.selenium.WebDriver;

public class MainPage extends BasePage {
    public final HeaderComponent headerComponent;

    public MainPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        log.debug("MainPage initialized");
    }

    public MainPage go() {
        log.info("Navigating to base URL: {}", baseURL);
        driver.get(baseURL);
        log.debug("Navigation completed");
        return this;
    }

}