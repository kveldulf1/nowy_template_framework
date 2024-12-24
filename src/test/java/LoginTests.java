import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.RegisterPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;
import java.util.Date;
import utils.CommonApiCalls;

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
        CommonApiCalls api = new CommonApiCalls();
        int userId = api.createUser();
        
        new WelcomePage(driver).go();
        api.setAuthCookies(driver, userId);
        
        driver.navigate().refresh();

        Assertions.assertTrue(new WelcomePage(driver).isUrlCorrect(),
                "Url is not correct after login.");
                
        // Clean up
        api.deleteUser(userId);
    }
}