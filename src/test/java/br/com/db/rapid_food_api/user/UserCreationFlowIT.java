package br.com.db.rapid_food_api.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import br.com.db.rapid_food_api.config.IntegrationTestBase;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserCreationFlowIT extends IntegrationTestBase{

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserSuccessfullyAndSaveIntoDB(){

        CreateUserRequest request = UserConstants.CREATE_USER_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("api/users")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Miguel Alves"))
            .body("active", equalTo(true));

        var savedUsers = userRepository.findAll();
        assertThat(savedUsers).hasSize(1);
        assertThat(savedUsers.get(0).getEmail()).isEqualTo("miguel@email.com");
        assertThat(savedUsers.get(0).getName()).isEqualTo("Miguel Alves");
        assertThat(savedUsers.get(0).getName()).isNotEqualTo("123456");
        assertThat(savedUsers.get(0).getActive()).isEqualTo(true);
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid(){

        CreateUserRequest request = UserConstants.CREATER_USER_REQUEST_WITH_INVALID_EMAIL;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("api/users")
        .then()
            .statusCode(400); 

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldReturn500WhenEmailAlreadyExistsInDb(){
        CreateUserRequest request = UserConstants.CREATE_USER_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("api/users")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Miguel Alves"))
            .body("active", equalTo(true));

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("api/users")
        .then()
            .statusCode(500);
            //.body("message", equalTo("Já existe um usuário cadastrado com o e-mail: miguel@gmail.com"));

        var savedUsers = userRepository.findAll();
        assertThat(savedUsers).hasSize(1);
    }
}
