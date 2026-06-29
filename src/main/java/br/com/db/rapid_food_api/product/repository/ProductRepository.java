package br.com.db.rapid_food_api.product.repository;

import br.com.db.rapid_food_api.product.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByVendorId(UUID vendorId);
    List<Product> findAllByVendorId(UUID vendorId, Pageable pageable);
}
