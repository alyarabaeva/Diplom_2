package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import request_ojects.CreateOrder;

import static io.restassured.RestAssured.given;

public class OrderStep {
    @Step("Creat new order with auth")
    public Response createOrderWithAuth(CreateOrder order, String token) {
        return given()
                .headers("authorization", token, "Content-type", "application/json; charset=utf-8")
                .and()
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Creat new order without auth")
    public Response createOrderWithoutAuth(CreateOrder order) {
        return given()
                .headers("Content-type", "application/json; charset=utf-8")
                .and()
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Get user orders")
    public Response getOrders(String token){
        return given()
                .headers("authorization", token)
                .get("/api/orders");
    }
}
