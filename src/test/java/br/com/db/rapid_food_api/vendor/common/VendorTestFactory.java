package br.com.db.rapid_food_api.vendor.common;

import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.dto.CreateVendorRequest;
import br.com.db.rapid_food_api.vendors.dto.VendorResponse;

import java.util.UUID;

public class VendorTestFactory {

    public static final UUID DEFAULT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final String DEFAULT_CNPJ = "06990590000123";
    public static final String DEFAULT_NAME = "Google Brasil";

    public static Vendor createValidVendor() {
        Vendor vendor = new Vendor();
        vendor.setId(DEFAULT_ID);
        vendor.setName(DEFAULT_NAME);
        vendor.setCnpj(DEFAULT_CNPJ);
        vendor.setActive(true);
        return vendor;
    }

    public static Vendor createVendor(){
        return new Vendor(null, DEFAULT_NAME, DEFAULT_CNPJ, true);
    }

    public static CreateVendorRequest createValidRequest() {
        return new CreateVendorRequest(DEFAULT_NAME, DEFAULT_CNPJ);
    }

    public static VendorResponse createValidResponse() {
        return new VendorResponse(DEFAULT_ID, DEFAULT_NAME, DEFAULT_CNPJ, true);
    }
}