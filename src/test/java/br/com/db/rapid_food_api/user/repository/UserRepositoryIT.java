package br.com.db.rapid_food_api.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.db.rapid_food_api.config.RepositoryIntegrationTestBase;
import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.repository.scenarios.ExistsByEmailAndIdNotScenario;
import br.com.db.rapid_food_api.user.repository.scenarios.ExistsByEmailScenario;

public class UserRepositoryIT extends RepositoryIntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb(){
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Tests for existsByEmail")
    class ExistsByEmail{

        @ParameterizedTest(name = "{0}")
        @EnumSource(ExistsByEmailScenario.class)
        @DisplayName("Should return expected result")
        void shouldReturnExpectedResult(ExistsByEmailScenario scenario){
            
            userRepository.saveAndFlush(buildUser(scenario.persistEmail));

            boolean result = userRepository.existsByEmail(scenario.queryEmail);

            assertThat(result).isEqualTo(scenario.expectedResult);
        }
    }

    @Nested
    @DisplayName("Tests for ExistsByEmailAndIdNot")
    class ExistsByEmailAndIdNot{

        @ParameterizedTest(name = "{0}")
        @EnumSource(ExistsByEmailAndIdNotScenario.class)
        @DisplayName("Should return expected result")
        void shouldReturnExpectedResult(ExistsByEmailAndIdNotScenario scenario){
            userRepository.saveAndFlush(buildUser("miguel@gmail.com"));
            User secondUser = userRepository.saveAndFlush(buildUser("outro@gmail.com"));

            boolean result = userRepository.existsByEmailAndIdNot(scenario.updatedEmail, secondUser.getId());
            assertThat(result).isEqualTo(scenario.expectedResult);
    }
}

    private User buildUser(String email) {
        return new User("Miguel Alves", email, "hash_senha");
    }
}
