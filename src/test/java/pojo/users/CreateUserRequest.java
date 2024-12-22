package pojo.users;
public class CreateUserRequest {
    private final String email;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String avatar;

    public CreateUserRequest(String email, String firstname, String lastname, String password, String avatar) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }
} 