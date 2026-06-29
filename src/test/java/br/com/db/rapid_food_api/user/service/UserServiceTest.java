package br.com.db.rapid_food_api.user.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static br.com.db.rapid_food_api.user.common.UserConstants.CREATE_USER_REQUEST;
import static br.com.db.rapid_food_api.user.common.UserConstants.EMAIL;
import static br.com.db.rapid_food_api.user.common.UserConstants.PASSWORD;
import static br.com.db.rapid_food_api.user.common.UserConstants.PASSWORD_HASH;
import static br.com.db.rapid_food_api.user.common.UserConstants.UPDATE_USER_REQUEST;
import static br.com.db.rapid_food_api.user.common.UserConstants.USER_ID;
import static br.com.db.rapid_food_api.user.common.UserConstants.createUser;
import static br.com.db.rapid_food_api.user.common.UserConstants.createUserResponse;
import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.dto.UserResponse;
import br.com.db.rapid_food_api.user.exception.EmailAlreadyExistsException;
import br.com.db.rapid_food_api.user.exception.UserNotFoundException;
import br.com.db.rapid_food_api.user.mapper.UserMapper;
import br.com.db.rapid_food_api.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

        @InjectMocks
        private UserService userService;

        @Mock
        private UserRepository userRepository;

        @Mock
        private UserMapper userMapper;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Test
        void shouldCreateUserWithValidData() {
                User user = createUser();
                UserResponse expectedResponse = createUserResponse();
                when(userRepository.existsByEmail(EMAIL))
                                .thenReturn(false);
                when(passwordEncoder.encode(PASSWORD))
                                .thenReturn(PASSWORD_HASH);
                when(userMapper.toEntity(CREATE_USER_REQUEST, PASSWORD_HASH))
                                .thenReturn(user);
                when(userRepository.save(any(User.class)))
                                .thenReturn(user);
                when(userMapper.toResponse(user))
                                .thenReturn(expectedResponse);

                UserResponse response = userService.create(CREATE_USER_REQUEST);

                assertThat(response).isNotNull();
                assertThat(response.name()).isEqualTo(CREATE_USER_REQUEST.name());
                assertThat(response.email()).isEqualTo(CREATE_USER_REQUEST.email());
                assertThat(response.active()).isTrue();
        }

        @Test
        void shouldThrowEmailAlreadyExistsExceptionWhenEmailAlreadyExists() {
                when(userRepository.existsByEmail(EMAIL))
                                .thenReturn(true);

                assertThatThrownBy(() -> userService.create(CREATE_USER_REQUEST))
                                .isInstanceOf(EmailAlreadyExistsException.class)
                                .hasMessage("Já existe um usuário cadastrado com o e-mail: miguel@email.com");
        }

        @Test
        void shouldGetUserByIdWhenUserExists() {
                User user = createUser();
                UserResponse expectedResponse = createUserResponse();

                when(userRepository.findById(USER_ID))
                                .thenReturn(Optional.of(user));

                when(userMapper.toResponse(user))
                                .thenReturn(expectedResponse);

                UserResponse response = userService.getById(USER_ID);

                assertThat(response).isNotNull();
                assertThat(response.id()).isEqualTo(expectedResponse.id());
                assertThat(response.name()).isEqualTo(expectedResponse.name());
                assertThat(response.email()).isEqualTo(expectedResponse.email());
                assertThat(response.active()).isTrue();
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExistById() {
                when(userRepository.findById(USER_ID))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getById(USER_ID))
                                .isInstanceOf(UserNotFoundException.class)
                                .hasMessage("User not found");
        }

        @Test
        void shouldUpdateUserWithValidData() {
                User user = createUser();

                when(userRepository.findById(USER_ID))
                                .thenReturn(Optional.of(user));
                when(userRepository.existsByEmailAndIdNot(UPDATE_USER_REQUEST.email(), USER_ID))
                                .thenReturn(false);
                when(passwordEncoder.encode(UPDATE_USER_REQUEST.password()))
                                .thenReturn(PASSWORD_HASH);
                when(userRepository.save(any(User.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));
                when(userMapper.toResponse(any(User.class)))
                                .thenAnswer(invocation -> {
                                        User updatedUser = invocation.getArgument(0);
                                        return new UserResponse(
                                                        updatedUser.getId(),
                                                        updatedUser.getName(),
                                                        updatedUser.getEmail(),
                                                        updatedUser.getActive(),
                                                        updatedUser.getCreatedAt());
                                });

                UserResponse response = userService.update(USER_ID, UPDATE_USER_REQUEST);

                assertThat(response).isNotNull();
                assertThat(response.name()).isEqualTo(UPDATE_USER_REQUEST.name());
                assertThat(response.email()).isEqualTo(UPDATE_USER_REQUEST.email());
                assertThat(response.active()).isEqualTo(UPDATE_USER_REQUEST.active());
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingUser() {
                when(userRepository.findById(USER_ID))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(() -> userService.update(USER_ID, UPDATE_USER_REQUEST))
                                .isInstanceOf(UserNotFoundException.class)
                                .hasMessage("User not found");
        }

        @Test
        void shouldThrowEmailAlreadyExistsExceptionWhenUpdatingUserWithExistingEmail() {
                when(userRepository.findById(USER_ID))
                                .thenReturn(Optional.of(createUser()));

                when(userRepository.existsByEmailAndIdNot(UPDATE_USER_REQUEST.email(), USER_ID))
                                .thenReturn(true);

                assertThatThrownBy(() -> userService.update(USER_ID, UPDATE_USER_REQUEST))
                                .isInstanceOf(EmailAlreadyExistsException.class)
                                .hasMessage("Já existe um usuário cadastrado com o e-mail: "
                                                + UPDATE_USER_REQUEST.email());
        }
}
