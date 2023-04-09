import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requestOjects.CreateUser;
import responseObjects.UserInfoResponse;
import responseObjects.UserReponse;
import steps.UserStep;

import static org.hamcrest.CoreMatchers.equalTo;
import static steps.URI.setUpURI;

public class UpdateUserTest {
    CreateUser user = new CreateUser("sasha@mail.ru", "1234567", "Sasha");
    UserStep step = new UserStep();

    @Before
    public void setUp() {
        setUpURI();
        step.creatUser(user).then().statusCode(200);
    }

    @Test
    public void UpdateAuthorizedUserEmailTest() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        String emailNew = "sasha-ya@mail.ru";
        Response updateResponse = step.updateUser(token, "email", emailNew);
        updateResponse.then().statusCode(200).and().body("success", equalTo(true));
        //Обновили почту
        user.setEmail(emailNew);
        UserReponse userInfo = updateResponse.as(UserInfoResponse.class).getUser();
        UserReponse userGetInfo = updateResponse.as(UserInfoResponse.class).getUser();
        Assert.assertEquals(userGetInfo.getEmail(), userInfo.getEmail());
        Assert.assertEquals(userGetInfo.getName(), userInfo.getName());
    }

    @Test
    public void UpdateAuthorizedUserNameTest() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        String nameNew = "Aleksandra";
        Response updateResponse = step.updateUser(token, "name", nameNew);
        updateResponse.then().statusCode(200).and().body("success", equalTo(true));
        //Обновили имя
        user.setName(nameNew);
        UserReponse userInfo = updateResponse.as(UserInfoResponse.class).getUser();
        UserReponse userGetInfo = updateResponse.as(UserInfoResponse.class).getUser();
        Assert.assertEquals(userGetInfo.getEmail(), userInfo.getEmail());
        Assert.assertEquals(userGetInfo.getName(), userInfo.getName());
    }

    @Test
    public void UpdateAuthorizedUserPasswordTest() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        String passwordNew = "Aa12345";
        Response updateResponse = step.updateUser(token, "password", passwordNew);
        updateResponse.then().statusCode(200).and().body("success", equalTo(true));
        //Обновили пароль
        user.setPassword(passwordNew);
    }

    @Test
    public void UpdateUnauthorizedUserEmailTest() throws JsonProcessingException {
        String emailNew = "sasha-ya@mail.ru";
        Response updateResponse = step.updateUser("token", "email", emailNew);
        updateResponse.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    public void UpdateUnauthorizedUserNameTest() throws JsonProcessingException {
        String nameNew = "Aleksandra";
        Response updateResponse = step.updateUser("token", "name", nameNew);
        updateResponse.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    public void UpdateUnauthorizedUserPasswordTest() throws JsonProcessingException {
        String passwordNew = "Aa12345";
        Response updateResponse = step.updateUser("token", "password", passwordNew);
        updateResponse.then().statusCode(401).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            step.deleteUser(token).then().statusCode(202);
        }
    }
}
