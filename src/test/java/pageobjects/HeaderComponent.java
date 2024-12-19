package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class HeaderComponent extends BasePage {
    private By loginDropdown = By.cssSelector("button[data-testid='btn-dropdown']");

    protected HeaderComponent(WebDriver driver) {
        super(driver);
        log.debug("HeaderComponent initialized");
    }

    public UserMenuComponent hoverMouseOverUserIcon() {
        log.debug("Hovering mouse over user icon");
        Actions actions = new Actions(driver);
        WebElement loginDropdownElement = driver.findElement(loginDropdown);
        actions.moveToElement(loginDropdownElement);
        actions.perform();
        log.debug("Mouse hover completed");
        return new UserMenuComponent(driver);
    }
}