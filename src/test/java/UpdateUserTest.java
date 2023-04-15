import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import request_ojects.CreateUser;
import response_objects.UserInfoResponse;
import response_objects.UserReponse;
import steps.UserStep;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static steps.URI.setUpURI;

public class UpdateUserTest {
    CreateUser user = new CreateUser("sasha@mail.ru", "1234567", "Sasha");
    UserStep step = new UserStep();

    @Before
    public void setUp() {
        setUpURI();
        step.creatUser(user).then().statusCode(SC_OK);
    }

    @Test
    public void updateAuthorizedUserEmailTest() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        String emailNew = "sasha-ya@mail.ru";
        Response updateResponse = step.updateUser(token, "email", emailNew);
        updateResponse.then().statusCode(SC_OK).and().body("success", equalTo(true));
        //Обновили почту
        user.setEmail(emailNew);
        UserReponse userInfo = updateResponse.as(UserInfoResponse.class).getUser();
        UserReponse userGetInfo = updateResponse.as(UserInfoResponse.class).getUser();
        Assert.assertEquals(userGetInfo.getEmail(), userInfo.getEmail());
        Assert.assertEquals(userGetInfo.getName(), userInfo.getName());
    }

    @Test
    public void updateAuthorizedUserNameTest() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        String nameNew = "Aleksandra";
        Response updateResponse = step.updateUser(token, "name", nameNew);
        updateResponse.then().statusCode(SC_OK).and().body("success", equalTo(true));
        //Обновили имя
        user.setName(nameNew);
        UserReponse userInfo = updateResponse.as(UserInfoResponse.class).getUser();
        UserReponse userGetInfo = updateResponse.as(UserInfoResponse.class).getUser();
        Assert.assertEquals(userGetInfo.getEmail(), userInfo.getEmail());
        Assert.assertEquals(userGetInfo.getName(), userInfo.getName());
    }

    @Test
    public void updateAuthorizedUserPasswordTest() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        String passwordNew = "Aa12345";
        Response updateResponse = step.updateUser(token, "password", passwordNew);
        updateResponse.then().statusCode(SC_OK).and().body("success", equalTo(true));
        //Обновили пароль
        user.setPassword(passwordNew);
    }

    @Test
    public void updateUnauthorizedUserEmailTest() throws JsonProcessingException {
        String emailNew = "sasha-ya@mail.ru";
        Response updateResponse = step.updateUser("token", "email", emailNew);
        updateResponse.then().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    public void updateUnauthorizedUserNameTest() throws JsonProcessingException {
        String nameNew = "Aleksandra";
        Response updateResponse = step.updateUser("token", "name", nameNew);
        updateResponse.then().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    public void updateUnauthorizedUserPasswordTest() throws JsonProcessingException {
        String passwordNew = "Aa12345";
        Response updateResponse = step.updateUser("token", "password", passwordNew);
        updateResponse.then().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            step.deleteUser(token).then().statusCode(SC_ACCEPTED);
        }
    }
}
