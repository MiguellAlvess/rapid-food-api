package br.com.db.rapid_food_api.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgresContainerConfig {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTEGRES = new PostgreSQLContainer<>("postgres:17.4");

}
