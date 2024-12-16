import org.junit.jupiter.api.Test;
import pageobjects.MainPage;
import pageobjects.UserMenu;

public class LoginTests extends BaseTest {
    @Test
    public void userLogsIn() throws InterruptedException {
        MainPage mainPage = new MainPage(driver).go();
        UserMenu userMenu = mainPage.headerComponent.hoverMouseOverUserIcon();
        userMenu.login();
    }
}