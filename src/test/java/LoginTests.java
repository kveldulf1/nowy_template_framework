import org.junit.jupiter.api.Test;
import pageobjects.MainPage;

public class LoginTests extends BaseTest {


    @Test
    public void userLogsIn() {
        MainPage mainPage = new MainPage(driver).go();
        mainPage.headerComponent.hoverMouseOverUserIcon();
    }
}
