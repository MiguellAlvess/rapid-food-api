package br.com.db.rapid_food_api.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.db.rapid_food_api.user.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
