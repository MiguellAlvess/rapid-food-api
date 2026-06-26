package br.com.db.rapid_food_api.order.repository;

import br.com.db.rapid_food_api.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
