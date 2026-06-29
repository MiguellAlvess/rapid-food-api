package br.com.db.rapid_food_api.vendors.service;

import br.com.db.rapid_food_api.vendors.domain.enums.Vendor;
import br.com.db.rapid_food_api.vendors.exceptions.VendorNotFoundException;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    @Transactional
    public Vendor create(Vendor vendor) {
        log.info("Criando novo restaurante com CNPJ: {}", vendor.getCnpj()); // apagar dps
        
        if (vendorRepository.existsByCnpj(vendor.getCnpj())) {
            throw new IllegalStateException("Já existe um restaurante cadastrado com este CNPJ.");
        }

        return vendorRepository.save(vendor);
    }

    @Transactional(readOnly = true)
    public Vendor findById(UUID id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Restaurante não encontrado com o ID: " + id));
    }

    @Transactional
    public void deactivate(UUID id) {
        Vendor vendor = findById(id);
        vendor.deactivate();
        vendorRepository.save(vendor);
        log.info("Restaurante ID {} inativado com sucesso.", id);
    }
}