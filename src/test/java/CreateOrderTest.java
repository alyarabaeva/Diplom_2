import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request_ojects.CreateUser;
import request_ojects.CreateOrder;
import steps.OrderStep;
import steps.UserStep;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static steps.URI.setUpURI;

public class CreateOrderTest {

    CreateUser user = new CreateUser("yarsasha@yandex.ru", "Aa12345", "Aleksandra");
    UserStep userStep = new UserStep();
    OrderStep orderStep = new OrderStep();
    private final List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72");

    @Before
    public void setUp() {
        setUpURI();
    }

    @Test
    public void createOrderAuthorizedUserTest() throws JsonProcessingException {
        userStep.creatUser(user).then().statusCode(SC_OK);
        String token = userStep.getAccessToken(user.getEmail(), user.getPassword());
        CreateOrder newOrder = new CreateOrder(ingredients);
        orderStep.createOrderWithAuth(newOrder, token)
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("order.ingredients", notNullValue())
                .body("order.owner.email", equalTo(user.getEmail()));
    }

    @Test
    public void createOrderUnauthorizedUserTest() {
        CreateOrder newOrder = new CreateOrder(ingredients);
        orderStep.createOrderWithoutAuth(newOrder)
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("order.ingredients", nullValue());
    }

    @After
    public void deleteUser() throws JsonProcessingException {
        String token = userStep.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            userStep.deleteUser(token).then().statusCode(SC_ACCEPTED);
        }
    }
}
