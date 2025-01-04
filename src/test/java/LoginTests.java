import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import pageobjects.MainPage;
import pageobjects.RegisterPage;
import pageobjects.WelcomePage;
import org.junit.jupiter.api.Assertions;
import utils.CommonApiCalls;

@Epic("Authentication")
@Feature("Login")
public class LoginTests extends BaseTest {

        @Test
        @Story("User Registration")
        @Description("Verify that new user can successfully register")
        @Severity(SeverityLevel.CRITICAL)
        public void registerUserTest() {
                new MainPage(driver)
                                .go()
                                .headerComponent
                                .hoverMouseOverUserIcon()
                                .clickOnRegisterButton()
                                .provideUserDetailsAndRegister();

                Assertions.assertTrue(new RegisterPage(driver).getAlertText().contains("User created"),
                                "User was not created");
        }

        @Test
        @Story("User Login")
        @Description("Verify that existing user can successfully login")
        @Severity(SeverityLevel.BLOCKER)
        public void userLogsIn() {
                WelcomePage welcomePage = new MainPage(driver)
                                .go()
                                .headerComponent
                                .hoverMouseOverUserIcon()
                                .clickLoginButton()
                                .loginAsRandomExistingUser();

                Assertions.assertTrue(welcomePage.isUrlCorrect(),
                                "Url is not correct after login. Current url: " + driver.getCurrentUrl());
        }

        @Test
        @Story("Invalid Login")
        @Description("Verify that system properly handles login with invalid credentials")
        @Severity(SeverityLevel.CRITICAL)
        public void userLogsInWithInvalidUser() {
                String alertText = new MainPage(driver)
                                .go()
                                .headerComponent
                                .hoverMouseOverUserIcon()
                                .clickLoginButton()
                                .loginAsInvalidUser()
                                .getAlertText();

                Assertions.assertTrue(alertText.contains("Invalid username or password"),
                                "Alert text does not contain 'Invalid email or password'");
        
        }

        @Test
        @Story("Login State")
        @Description("Verify that logged in user is redirected to correct page")
        @Severity(SeverityLevel.NORMAL)
        public void loggedInUserIsOnTheRightPage() {
                new CommonApiCalls().goToWelcomePageAsLoggedInUser(driver);

                Assertions.assertTrue(new WelcomePage(driver).isUrlCorrect(),
                                "Url is not correct after login.");
        }
}