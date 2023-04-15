package response_objects;

public class AuthUserResponse {

    private String accessToken;
    private String refreshToken;
    private UserReponse user;

    public UserReponse getUser() {
        return user;
    }

    public void setUser(UserReponse user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
