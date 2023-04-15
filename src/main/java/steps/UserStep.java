package steps;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import request_ojects.CreateUser;
import response_objects.AuthUserResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserStep {
    @Step("Creat new user")
    public Response creatUser(CreateUser user) {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Creat new user from json")
    public Response creatUserWithJson(String json) {
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .and()
                .body(json)
                .when()
                .post("/api/auth/register");
    }

    @Step("Login user")
    public Response loginUser(String email, String password) throws JsonProcessingException {
        String json = getJsonWithTwoParams("email", email, "password", password);
        return given()
                .header("Content-type", "application/json; charset=utf-8")
                .and()
                .body(json)
                .when()
                .post("/api/auth/login");
    }

    @Step("Get access token")
    public String getAccessToken(String email, String password) throws JsonProcessingException {
        return loginUser(email, password).as(AuthUserResponse.class).getAccessToken();

    }


    @Step("Delete user")
    public Response deleteUser(String token) {
        return given()
                .headers("authorization", token)
                .delete("/api/auth/user");
    }

    @Step("Update user")
    public Response updateUser(String token, String field, String value) throws JsonProcessingException {
        String json = getJsonWithOneParam(field, value);
        return given()
                .headers("authorization", token, "Content-type", "application/json; charset=utf-8")
                .and()
                .body(json)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Get user information")
    public Response getUserInformation(String token) {
        return given()
                .headers("authorization", token)
                .get("/api/auth/user");
    }

    public String getJsonWithTwoParams(String param1, String value1, String param2, String value2) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map.put(param1, value1);
        map.put(param2, value2);
        return objectMapper.writeValueAsString(map);
    }

    public String getJsonWithOneParam(String param1, String value1) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map.put(param1, value1);
        return objectMapper.writeValueAsString(map);
    }
}
