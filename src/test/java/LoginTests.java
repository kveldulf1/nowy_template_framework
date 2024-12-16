import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;

public class LoginTests extends BaseTest {
    @Test
    public void userLogsIn() {
        WelcomePage welcomePage = new MainPage(driver)
            .go()
            .headerComponent
            .hoverMouseOverUserIcon()
            .login();
            
        Assertions.assertTrue(welcomePage.isUrlCorrect(), 
            "Url is not correct after login.");
    }
}