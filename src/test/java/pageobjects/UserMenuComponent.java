package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UserMenuComponent extends BasePage {
    private By loginButton = By.cssSelector("a#loginBtn");
    private By registerButton = By.cssSelector("a#registerBtn");

    public UserMenuComponent(WebDriver driver) {
        super(driver);
        log.debug("UserMenuComponent initialized");
    }

    public LoginPage clickLoginButton() {
        log.debug("Clicking login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(loginButton).click();
        return new LoginPage(driver);
    }

    public RegisterPage clickOnRegisterButton() {
        log.debug("Clicking register button");
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        driver.findElement(registerButton).click();
        return new RegisterPage(driver);
    }
}