package br.com.db.rapid_food_api.user.client;

import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.dto.UpdateUserRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_PATH = "api/users";

    public ValidatableResponse createUser(CreateUserRequest request){
        return given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post(BASE_PATH)
        .then();
    }

    public ValidatableResponse updateUser(UpdateUserRequest request, String id){
        return given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .patch(BASE_PATH + "/{id}", id)
        .then();
    }

    public ValidatableResponse getUserById(String id){
        return given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", id)
        .then();
    }

    public String createAndGetId(CreateUserRequest request){
        return createUser(request)
            .statusCode(201)
            .extract()
            .path("id").toString();
    }
}
