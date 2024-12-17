import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginTests extends BaseTest {
     private static final Logger log = LoggerFactory.getLogger(LoginTests.class);

    @Test
    public void userLogsIn() {
        log.info("Starting login test");
        
        WelcomePage welcomePage = new MainPage(driver)
            .go()
            .headerComponent
            .hoverMouseOverUserIcon()
            .login();
            
        log.info("Verifying login success");
        Assertions.assertTrue(welcomePage.isUrlCorrect(), 
            "Url is not correct after login.");
        
        log.info("Login test completed successfully");
    }
}