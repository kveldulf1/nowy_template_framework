import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.RegisterPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;
import java.util.Date;

public class LoginTests extends BaseTest {

    @Test
    public void registerUserTest() {
        WelcomePage welcomePage = new MainPage(driver)
                .go().headerComponent
                .hoverMouseOverUserIcon()
                .clickOnRegisterButton()
                .provideUserDetailsAndRegister();

        Assertions.assertTrue(new RegisterPage(driver).getAlertText().contains("User created"),
                "User was not created");
    }

    @Test
    public void userLogsIn() {
        WelcomePage welcomePage = new MainPage(driver)
                .go().headerComponent
                .hoverMouseOverUserIcon()
                .clickLoginButton()
                .login();

        String accessToken = System.getProperty("access_token");
        System.out.println("Access Token: " + accessToken);

        Assertions.assertTrue(welcomePage.isUrlCorrect(),
                "Url is not correct after login.");
    }

    @Test
    public void loggedInUserIsOnTheRightPage() {
        // First navigate to the page
        new WelcomePage(driver).go();
        // Add cookies line by line
        Cookie avatar = new Cookie("avatar", "TestUser");
        Cookie email = new Cookie("email", "test_1735058042907@example.com");
        Cookie expires = new Cookie("expires", "1735061679558");
        Cookie firstname = new Cookie("firstname", "TestUser");
        Cookie id = new Cookie("id", "18");
        Cookie pmaLang = new Cookie("pma_lang", "en");
        Cookie token = new Cookie("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
        Cookie username = new Cookie("username", "test_1735058042907@example.com");
        Cookie versionStatus = new Cookie("versionStatus", "1");

        driver.manage().addCookie(avatar);
        driver.manage().addCookie(email);
        driver.manage().addCookie(expires);
        driver.manage().addCookie(firstname);
        driver.manage().addCookie(id);
        driver.manage().addCookie(pmaLang);
        driver.manage().addCookie(token);
        driver.manage().addCookie(username);
        driver.manage().addCookie(versionStatus);

        // Refresh to apply cookies
        driver.navigate().refresh();

        Assertions.assertTrue(new WelcomePage(driver).isUrlCorrect(),
                "Url is not correct after login.");
    }
}