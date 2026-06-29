package br.com.db.rapid_food_api.vendors.mapper;

import br.com.db.rapid_food_api.vendors.dto.CreateVendorRequest;
import br.com.db.rapid_food_api.vendors.dto.VendorResponse;
import br.com.db.rapid_food_api.vendors.domain.enums.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VendorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Vendor toEntity(CreateVendorRequest request);

    VendorResponse toResponse(Vendor vendor);
}