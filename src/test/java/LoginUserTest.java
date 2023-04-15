import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import request_ojects.CreateUser;
import response_objects.AuthUserResponse;
import steps.UserStep;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static steps.URI.setUpURI;

public class LoginUserTest {

    CreateUser user = new CreateUser("sasha-ya-test@yandex.ru", "1234567", "Sasha");
    UserStep step = new UserStep();

    @Before
    public void setUp() {
        setUpURI();
        step.creatUser(user);
    }

    @Test
    public void loginUserPositiveTest() throws JsonProcessingException {
        Response loginResponse = step.loginUser(user.getEmail(), user.getPassword());
        loginResponse.then().statusCode(SC_OK);
        String loginEmail = loginResponse.as(AuthUserResponse.class).getUser().getEmail();
        String loginName = loginResponse.as(AuthUserResponse.class).getUser().getName();

        Assert.assertEquals("Ошибка логина пользователя", user.getEmail(), loginEmail);
        Assert.assertEquals("Ошибка логина пользователя", user.getName(), loginName);
    }

    @Test
    public void loginUserWithIncorrectEmailTest() throws JsonProcessingException {
        step.loginUser("123" + user.getEmail(), user.getPassword())
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginUserWithIncorrectPasswordTest() throws JsonProcessingException {
        step.loginUser(user.getEmail(), user.getPassword() + "test")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteUser() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            step.deleteUser(token).then().statusCode(SC_ACCEPTED);
        }
    }

}
