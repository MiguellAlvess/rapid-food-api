package br.com.db.rapid_food_api.product.controller;

import br.com.db.rapid_food_api.product.dto.ProductRequestDto;
import br.com.db.rapid_food_api.product.dto.ProductResponseDto;
import br.com.db.rapid_food_api.product.dto.ProductUpdateDto;
import br.com.db.rapid_food_api.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto,
                                                            UriComponentsBuilder builder) {
        ProductResponseDto responseDto = productService.createProduct(requestDto);
        URI uri = builder.path("/api/products/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(productService.getAllProducts(vendorId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody @Valid ProductUpdateDto updateDto,
                                                            @PathVariable UUID id){
        return ResponseEntity.ok(productService.updateProduct(updateDto, id));
    }
}
