package br.com.db.rapid_food_api.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import br.com.db.rapid_food_api.config.IntegrationTestBase;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.dto.UpdateUserRequest;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserUpdateFlowIT extends IntegrationTestBase {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturn200AndUpdateUser() {

        CreateUserRequest createRequest = UserConstants.CREATE_USER_REQUEST;
        UpdateUserRequest updateRequest = UserConstants.UPDATE_USER_REQUEST;

        String userId = given()
                    .contentType(ContentType.JSON)
                    .body(createRequest)
                .when()
                    .post("/api/users")
                    .then()
                .statusCode(201)
                .extract()
                .path(("id")).toString();

            given()
                    .contentType(ContentType.JSON)
                    .body(updateRequest)
                .when()
                    .patch("/api/users/{id}", userId)
                    .then()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo("Miguel Atualizado"))
                .body("email",  equalTo("miguel.atualizado@email.com"));
       
        var updatedUser = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("Miguel Atualizado");
        assertThat(updatedUser.getEmail()).isEqualTo("miguel.atualizado@email.com");
    }
}
