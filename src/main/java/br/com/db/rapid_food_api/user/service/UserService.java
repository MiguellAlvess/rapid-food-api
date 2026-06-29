package br.com.db.rapid_food_api.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.dto.UserResponse;
import br.com.db.rapid_food_api.user.exception.EmailAlreadyExistsException;
import br.com.db.rapid_food_api.user.mapper.UserMapper;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }
        String passwordHash = passwordEncoder.encode(request.password());
        User user = userMapper.toEntity(request, passwordHash);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(new User("nome", "email", "senha"));
    }
}
