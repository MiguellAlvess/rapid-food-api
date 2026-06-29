package br.com.db.rapid_food_api.vendors.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.db.rapid_food_api.vendors.domain.enums.Vendor;
import br.com.db.rapid_food_api.vendors.dto.CreateVendorRequest;
import br.com.db.rapid_food_api.vendors.dto.VendorResponse;
import br.com.db.rapid_food_api.vendors.mapper.VendorMapper;
import br.com.db.rapid_food_api.vendors.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
    private final VendorMapper vendorMapper;

    @PostMapping
    public ResponseEntity<VendorResponse> create(@RequestBody @Valid CreateVendorRequest request) {
        Vendor vendorToSave = vendorMapper.toEntity(request);
        Vendor savedVendor = vendorService.create(vendorToSave);
        VendorResponse response = vendorMapper.toResponse(savedVendor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> findById(@PathVariable UUID id) {
        Vendor vendor = vendorService.findById(id);
        return ResponseEntity.ok(vendorMapper.toResponse(vendor));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        vendorService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
    
}
