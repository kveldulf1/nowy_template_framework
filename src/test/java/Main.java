import utils.CommonApiCalls;

public class Main {

    public static void main(String[] args) {
        // test new code here
        CommonApiCalls commonApiCalls = new CommonApiCalls();
        commonApiCalls.logInAndGetAccessTokenForUser(commonApiCalls.createUser());
        
    }
}
