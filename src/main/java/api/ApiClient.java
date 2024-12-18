package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient {
    private static final String BASE_URL = "http://jsonplaceholder.typicode.com";

    public static Response getUsers() {
        return RestAssured.get(BASE_URL + "/users");
    }

    public static Response getTodos() {
        return RestAssured.get(BASE_URL + "/todos");
    }
}
