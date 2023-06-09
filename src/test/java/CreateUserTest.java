import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request_ojects.CreateUser;
import steps.UserStep;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.URI.setUpURI;

public class CreateUserTest {
    CreateUser user = new CreateUser("sasha-ya@yandex.ru", "Aa12345", "Sasha");
    UserStep step = new UserStep();

    @Before
    public void setUp() {
        setUpURI();
    }

    @Test
    public void createUserPositiveTest() {
        step.creatUser(user)
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("accessToken", notNullValue());
    }

    @Test
    public void createDuplicateUserTest() {
        step.creatUser(user)
                .then()
                .statusCode(SC_OK);
        step.creatUser(user)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    public void createUserWithoutEmailTest() throws JsonProcessingException {
        user.setEmail("");
        step.creatUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPasswordTest() throws JsonProcessingException {
        user.setPassword("");
        step.creatUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutNameTest() throws JsonProcessingException {
        user.setName("");
        step.creatUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @After
    public void deleteUser() throws JsonProcessingException {
        String token = step.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            step.deleteUser(token);
        }
    }

}

