package br.com.db.rapid_food_api.product.service;

import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.product.dto.ProductRequestDto;
import br.com.db.rapid_food_api.product.dto.ProductResponseDto;
import br.com.db.rapid_food_api.product.dto.ProductUpdateDto;
import br.com.db.rapid_food_api.product.mapper.ProductMapper;
import br.com.db.rapid_food_api.product.repository.ProductRepository;
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

    public ProductResponseDto createProduct(@Valid ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll() // precisa refatorar para findByVendorId;
                                .stream().map(productMapper::toDto).toList();
    }

    @Transactional
    public ProductResponseDto updateProduct(@Valid ProductUpdateDto updateDto, UUID id) {
        // procurar Vendor
        Product product = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found with id: " + id));
        productMapper.updateProduct(product, updateDto);
        product.setUpdatedAt(LocalDateTime.now());
        return productMapper.toDto(product);
    }
}
