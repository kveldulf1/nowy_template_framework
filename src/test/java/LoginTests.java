import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.RegisterPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;

public class LoginTests extends BaseTest {
        
    @Test
    public void registerUserTest() {
        WelcomePage welcomePage = new MainPage(driver)
            .go()
            .headerComponent
            .hoverMouseOverUserIcon()
            .clickOnRegisterButton()
            .provideUserDetailsAndRegister();

        Assertions.assertTrue(new RegisterPage(driver).getAlertText().contains("User created"),
                "User was not created");
    }
    
    @Test
    public void userLogsIn() {
        WelcomePage welcomePage = new MainPage(driver)
                .go()
                .headerComponent
                .hoverMouseOverUserIcon()
                .clickLoginButton()
                .login();

        Assertions.assertTrue(welcomePage.isUrlCorrect(),
                "Url is not correct after login.");
    }
}