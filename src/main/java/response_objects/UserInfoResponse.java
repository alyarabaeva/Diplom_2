package response_objects;

public class UserInfoResponse {
    private String message;
    private UserReponse user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserReponse getUser() {
        return user;
    }

    public void setUser(UserReponse user) {
        this.user = user;
    }
}
