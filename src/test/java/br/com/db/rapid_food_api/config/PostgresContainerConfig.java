package br.com.db.rapid_food_api.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;


public class PostgresContainerConfig {

    @ServiceConnection
    static final PostgreSQLContainer<?> POSTEGRES = new PostgreSQLContainer<>("postgres:17.4");

    static{
        POSTEGRES.start();
    }
}
