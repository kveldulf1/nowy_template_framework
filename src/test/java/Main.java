import utils.CommonApiCalls;

public class Main {

    public static void main(String[] args) {
        CommonApiCalls utils = new CommonApiCalls();
        int userId = utils.createUser();
        utils.getAccessTokenForUser(userId);
    }   
}
