package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.TestDataManager;
import pojo.users.CreateUserRequest;

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

    public WelcomePage login() {
        log.info("Performing login with default credentials");
        CreateUserRequest loginData = TestDataManager.getTestData("api/requests/login", CreateUserRequest.class);
        return login(loginData.getEmail(), loginData.getPassword());
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
} 