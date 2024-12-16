package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WelcomePage extends BasePage {
    private static final String EXPECTED_URL = "http://localhost:3000/welcome";
    
    public WelcomePage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isUrlCorrect() {
        return driver.getCurrentUrl().equals(EXPECTED_URL);
    }
}