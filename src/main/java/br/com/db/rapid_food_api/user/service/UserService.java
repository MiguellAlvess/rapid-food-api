package br.com.db.rapid_food_api.user.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.dto.CreateUserRequest;
import br.com.db.rapid_food_api.user.dto.UpdateUserRequest;
import br.com.db.rapid_food_api.user.dto.UserResponse;
import br.com.db.rapid_food_api.user.exception.EmailAlreadyExistsException;
import br.com.db.rapid_food_api.user.exception.UserNotFoundException;
import br.com.db.rapid_food_api.user.mapper.UserMapper;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

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

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (request.email() != null && userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new EmailAlreadyExistsException(request.email());
        }
        String passwordHash = null;
        if (request.password() != null) {
            passwordHash = passwordEncoder.encode(request.password());
        }
        user.update(
                request.name(),
                request.email(),
                passwordHash,
                request.active());
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
