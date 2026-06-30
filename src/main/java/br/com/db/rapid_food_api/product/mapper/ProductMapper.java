package br.com.db.rapid_food_api.product.mapper;

import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.product.dto.ProductRequestDto;
import br.com.db.rapid_food_api.product.dto.ProductResponseDto;
import br.com.db.rapid_food_api.product.dto.ProductUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequestDto productRequestDto);

    @Mapping(target = "vendorName", source = "product.vendor.name")
    ProductResponseDto toDto(Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateDto updateDto);
}
