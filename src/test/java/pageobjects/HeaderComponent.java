package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HeaderComponent extends BasePage {
    private By loginButton = By.cssSelector("#loginBtn");
    private By loginDropdown = By.cssSelector("button[data-testid='btn-dropdown']");

    protected HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public UserMenu hoverMouseOverUserIcon() {
        Actions actions = new Actions(driver);
        WebElement loginDropdownElement = driver.findElement(loginDropdown);
        actions.moveToElement(loginDropdownElement);
        actions.perform();
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(loginButton).click();
        return new UserMenu(driver);
    }
}