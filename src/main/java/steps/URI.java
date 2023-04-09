package steps;

import io.restassured.RestAssured;

public class URI {
    public static final String PROD_URI = "https://stellarburgers.nomoreparties.site";

    public static void setUpURI(){
        RestAssured.baseURI = PROD_URI;
    }
}
