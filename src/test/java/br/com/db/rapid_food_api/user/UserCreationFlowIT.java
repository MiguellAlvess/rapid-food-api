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
import br.com.db.rapid_food_api.user.client.UserClient;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import io.restassured.RestAssured;

public class UserCreationFlowIT extends IntegrationTestBase{

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private UserClient userClient;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
        userClient = new UserClient();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserSuccessfullyAndSaveIntoDB(){
        CreateUserRequest request = UserConstants.CREATE_USER_REQUEST;

        userClient.createUser(request)
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Miguel Alves"))
            .body("email", equalTo("miguel@email.com"))
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

        userClient.createUser(request)
            .statusCode(400); 

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldReturn500WhenEmailAlreadyExistsInDb(){
        CreateUserRequest request = UserConstants.CREATE_USER_REQUEST;

        userClient.createUser(request).statusCode(201);
        userClient.createUser(request).statusCode(500);
            //.body("message", equalTo("Já existe um usuário cadastrado com o e-mail: miguel@gmail.com"));

        var savedUsers = userRepository.findAll();
        assertThat(savedUsers).hasSize(1);
    }
}