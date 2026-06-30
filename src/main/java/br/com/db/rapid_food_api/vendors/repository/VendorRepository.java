package br.com.db.rapid_food_api.vendors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.db.rapid_food_api.vendors.domain.Vendor;

import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    boolean existsByCnpj(String cnpj);
}