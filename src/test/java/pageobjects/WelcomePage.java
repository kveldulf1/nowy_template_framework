package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class WelcomePage extends BasePage {
    private By welcomeMessage = By.cssSelector(".welcome-message");
    private By logoutButton = By.cssSelector("#logoutButton");
    private By userProfile = By.cssSelector(".user-profile");

    private final String expectedUrl = "http://localhost:3000/welcome";

    public WelcomePage(WebDriver driver) {
        super(driver);
    }
    

    public boolean checkUrl() {
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        return driver.getCurrentUrl().equals(expectedUrl);
    }
}