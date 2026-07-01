package br.com.db.rapid_food_api.user.mapper;

import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request, String passwordHash) {
        return new User(request.name(), request.email(), passwordHash);
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getActive(), user.getCreatedAt());
    }
}
