package br.com.db.rapid_food_api.user.common;

import java.util.UUID;

import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.dto.UserResponse;

public class UserConstants {

    public static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    public static final String NAME = "Miguel Alves";
    public static final String EMAIL = "miguel@email.com";
    public static final String PASSWORD = "123456";
    public static final String PASSWORD_HASH = "$2a$10$hashedPassword";

    public static final CreateUserRequest CREATE_USER_REQUEST = new CreateUserRequest(
            NAME,
            EMAIL,
            PASSWORD);

    public static User createUser() {
        return new User(
                NAME,
                EMAIL,
                PASSWORD_HASH);
    }

    public static UserResponse createUserResponse() {
        return new UserResponse(
                USER_ID,
                NAME,
                EMAIL,
                true,
                null);
    }

    private UserConstants() {
    }
}
