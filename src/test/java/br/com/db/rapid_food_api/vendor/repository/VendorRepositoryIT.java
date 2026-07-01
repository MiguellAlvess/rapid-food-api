package br.com.db.rapid_food_api.vendor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.db.rapid_food_api.config.RepositoryIntegrationTestBase;
import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;

public class VendorRepositoryIT extends RepositoryIntegrationTestBase{

    @Autowired
    VendorRepository vendorRepository;
     
    @BeforeEach
    void cleanDb(){
        vendorRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return false when CNPJ does not exists")
    void shouldReturnFalseWhenCnpjDoesNotExist(){
        Vendor vendor = VendorTestFactory.createVendor();
        String cnpj = "06990590000122";

        vendorRepository.saveAndFlush(vendor);
        boolean result = vendorRepository.existsByCnpj(cnpj);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when CNPJ already exists")
    void shouldReturnTrueForCNPJsThatAlreadyExists(){
        Vendor vendor = VendorTestFactory.createVendor();

        vendorRepository.saveAndFlush(vendor);
        boolean result = vendorRepository.existsByCnpj(vendor.getCnpj());

        assertThat(result).isTrue();
    }
}
