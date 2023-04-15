import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import request_ojects.CreateUser;
import request_ojects.CreateOrder;
import steps.OrderStep;
import steps.UserStep;

import static org.apache.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static steps.URI.setUpURI;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {
    CreateUser user = new CreateUser("ya-sasha@yandex.ru", "Aa12345", "Aleksandra");
    UserStep userStep = new UserStep();
    OrderStep orderStep = new OrderStep();
    private final List<String> ingredients;
    private final int statusCode;

    public CreateOrderParameterizedTest(List<String> ingredients, int statusCode) {
        this.ingredients = ingredients;
        this.statusCode = statusCode;
    }

    @Before
    public void setUp() {
        setUpURI();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new ArrayList<>(), SC_BAD_REQUEST},
                {Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"), SC_OK},
                {Arrays.asList("61c0c5a71d1f82001bdaaa6d"), SC_OK},
                {Arrays.asList("abcde", "abcd"), SC_INTERNAL_SERVER_ERROR}
        };
    }

    @Test
    public void createOrderAuthorizedUserTest() throws JsonProcessingException {
        userStep.creatUser(user).then().statusCode(SC_OK);
        String token = userStep.getAccessToken(user.getEmail(), user.getPassword());
        CreateOrder newOrder = new CreateOrder(ingredients);
        orderStep.createOrderWithAuth(newOrder, token)
                .then()
                .statusCode(statusCode);
    }

    @Test
    public void createOrderUnauthorizedUserTest() {
        CreateOrder newOrder = new CreateOrder(ingredients);
        orderStep.createOrderWithoutAuth(newOrder)
                .then()
                .statusCode(statusCode);
    }

    @After
    public void deleteUser() throws JsonProcessingException {
        String token = userStep.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            userStep.deleteUser(token).then().statusCode(SC_ACCEPTED);
        }
    }
}
