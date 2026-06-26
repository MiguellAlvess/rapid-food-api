package br.com.db.rapid_food_api.product.repository;

import br.com.db.rapid_food_api.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
