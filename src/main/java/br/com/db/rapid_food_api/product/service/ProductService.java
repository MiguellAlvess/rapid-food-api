package br.com.db.rapid_food_api.product.service;

import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.product.dto.ProductRequestDto;
import br.com.db.rapid_food_api.product.dto.ProductResponseDto;
import br.com.db.rapid_food_api.product.dto.ProductUpdateDto;
import br.com.db.rapid_food_api.product.exception.BusinessException;
import br.com.db.rapid_food_api.product.mapper.ProductMapper;
import br.com.db.rapid_food_api.product.repository.ProductRepository;
import br.com.db.rapid_food_api.vendors.domain.enums.Vendor;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final VendorRepository vendorRepository;

    private Vendor findVendorById(UUID id){
        return vendorRepository.findById(id)
           .orElseThrow(() -> new EntityNotFoundException("Vendor with id " + id + " not found"));
    }

    private Product findProduct(UUID id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public ProductResponseDto createProduct(@Valid ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
        Vendor vendor = findVendorById(requestDto.vendorId());
        product.setVendor(vendor);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll() // precisa refatorar para findByVendorId;
                                .stream().map(productMapper::toDto).toList();
    }

    @Transactional
    public ProductResponseDto updateProduct(@Valid ProductUpdateDto updateDto, UUID id) {
        Vendor vendor = findVendorById(id);
        Product product = findProduct(id);
        if (vendor.getId() != product.getVendor().getId()) {
            throw new BusinessException("This product is not owner of this vendor");
        }
        productMapper.updateProduct(product, updateDto);
        product.setUpdatedAt(LocalDateTime.now());
        return productMapper.toDto(product);
    }
}
