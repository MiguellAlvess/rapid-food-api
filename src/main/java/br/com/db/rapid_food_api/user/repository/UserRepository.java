package br.com.db.rapid_food_api.user.repository;

import br.com.db.rapid_food_api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
