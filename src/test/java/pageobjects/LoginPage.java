package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.gson.JsonObject;

import helpers.TestDataReader;

public class LoginPage extends BasePage {
    private By loginInput = By.cssSelector("input#username");
    private By passwordInput = By.cssSelector("input#password");
    private By loginButton = By.cssSelector("input#loginButton");

    public LoginPage(WebDriver driver) {
        super(driver);
        log.debug("LoginPage initialized");
    }

    private void typeUsername(String username) {
        log.debug("Typing username: {}", username);
        wait.until(ExpectedConditions.elementToBeClickable(loginInput));
        driver.findElement(loginInput).sendKeys(username);
    }

    private void typePassword(String password) {
        log.debug("Typing password: {}", "*".repeat(password.length()));
        wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        driver.findElement(passwordInput).sendKeys(password);
    }

    public WelcomePage login(String email, String password) {
        log.info("Performing login with email: {}", email);
        typeUsername(email);
        typePassword(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(loginButton).click();
        log.info("Login completed");
        return new WelcomePage(driver);
    }

    public WelcomePage loginAsRandomExistingUser() {
        JsonObject randomUser = TestDataReader.getRandomUser();
        log.info("Logging in as random existing user: {}, password: {}",
                randomUser.get("email").getAsString(),
                randomUser.get("password").getAsString());
        return login(randomUser.get("email").getAsString(), randomUser.get("password").getAsString());

    }
}