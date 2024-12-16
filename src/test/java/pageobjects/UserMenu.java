package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UserMenu extends BasePage {
    private By loginInput = By.cssSelector("input#username");
    private By passwordInput = By.cssSelector("input#password");
    private By loginButton = By.cssSelector("input#loginButton");

    private String username = "damagehcmf@gmail.com";
    private String password = "dupadupa123";

    public UserMenu(WebDriver driver) {
        super(driver);
    }

    private void typeUsername(String username) {
        wait.until(ExpectedConditions.elementToBeClickable(loginInput));
        driver.findElement(loginInput).sendKeys(username);
    }

    private void typePassword(String password) {
        wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        driver.findElement(passwordInput).sendKeys(password);
    }

    private void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(loginButton).click();
    }

    public WelcomePage login(){
        typeUsername(username);
        typePassword(password);
        clickLoginButton();
        return new WelcomePage(driver);
    }
}