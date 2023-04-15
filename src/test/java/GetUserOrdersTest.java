import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import request_ojects.CreateUser;
import request_ojects.CreateOrder;
import response_objects.CreateOrderResponse;
import response_objects.GetOrdersResponse;
import steps.OrderStep;
import steps.UserStep;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static steps.URI.setUpURI;

public class GetUserOrdersTest {
    CreateUser user = new CreateUser("yar-sasha@yandex.ru", "Aa12345", "Aleksandra");
    UserStep userStep = new UserStep();
    OrderStep orderStep = new OrderStep();
    private final List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72");

    @Before
    public void setUp() {
        setUpURI();
        userStep.creatUser(user).then().statusCode(SC_OK);
    }

    @Test
    public void getOrdersAuthorizedUserTest() throws JsonProcessingException {
        String token = userStep.getAccessToken(user.getEmail(), user.getPassword());
        CreateOrder newOrder = new CreateOrder(ingredients);
        CreateOrderResponse orderExpected = orderStep.createOrderWithAuth(newOrder, token).as(CreateOrderResponse.class);

        Response getOrderResponse = orderStep.getOrders(token);

        getOrderResponse.then().statusCode(SC_OK);
        GetOrdersResponse orderActual = getOrderResponse.as(GetOrdersResponse.class);
        Assert.assertEquals("Номера заказов не совпадают", orderExpected.getOrder().getNumber(), orderActual.getOrders().get(0).getNumber());
        Assert.assertEquals("Id заказов не совпадают", orderExpected.getOrder().get_id(), orderActual.getOrders().get(0).get_id());
    }

    @Test
    public void getOrdersUnauthorizedUserTest() {
        orderStep.getOrders("token")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() throws JsonProcessingException {
        String token = userStep.getAccessToken(user.getEmail(), user.getPassword());
        if (token != null) {
            userStep.deleteUser(token).then().statusCode(SC_ACCEPTED);
        }
    }
}
