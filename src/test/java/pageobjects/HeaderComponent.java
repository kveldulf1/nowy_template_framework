package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HeaderComponent extends BasePage {

    protected HeaderComponent(WebDriver driver) {
        super(driver);
    }

    By loginButton = By.cssSelector("#loginBtn");
    By loginDropdown = By.cssSelector("button[data-testid='btn-dropdown']");

    public UserMenu hoverMouseOverUserIcon() {
        Actions actions = new Actions(driver);
        WebElement loginDropdownElement = driver.findElement(loginDropdown);
        actions.moveToElement(loginDropdownElement);
        actions.perform();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(loginButton).click();

        return new UserMenu(driver);
    }
}
