package pageobjects;

import org.openqa.selenium.WebDriver;

public class MainPage extends BasePage {
    public final HeaderComponent headerComponent;

    public MainPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
    }

    public MainPage go() {
        driver.get(baseURL);
        return this;
    }

}