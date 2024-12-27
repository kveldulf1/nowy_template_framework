import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.RegisterPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;
import utils.CommonApiCalls;
import utils.TestDataManager;
import pojo.users.CreateUserRequest;

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
        // Pobierz dane logowania z JSON
        CreateUserRequest loginData = TestDataManager.getTestData("api/requests/login", CreateUserRequest.class);
        
        WelcomePage welcomePage = new MainPage(driver)
                .go()
                .headerComponent
                .hoverMouseOverUserIcon()
                .clickLoginButton()
                .login(loginData.getEmail(), loginData.getPassword());

        Assertions.assertTrue(welcomePage.isUrlCorrect(),
                "Url is not correct after login.");
    }

    @Test
    public void loggedInUserIsOnTheRightPage() {
        new CommonApiCalls().goToWelcomePageAsLoggedInUser(driver);

        Assertions.assertTrue(new WelcomePage(driver).isUrlCorrect(),
                "Url is not correct after login.");
    }
}