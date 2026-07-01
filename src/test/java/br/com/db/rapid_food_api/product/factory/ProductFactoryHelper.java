package br.com.db.rapid_food_api.product.factory;

import br.com.db.rapid_food_api.product.dto.ProductRequestDto;
import br.com.db.rapid_food_api.product.dto.ProductResponseDto;
import br.com.db.rapid_food_api.product.dto.ProductUpdateDto;
import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductFactoryHelper {

    public static final UUID VENDOR_ID = VendorTestFactory.DEFAULT_ID;
    public static final String VENDOR_NAME = VendorTestFactory.DEFAULT_NAME;

    public static final UUID PRODUCT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final String PRODUCT_NAME = "Hambúrguer Artesanal";
    public static final String PRODUCT_DESCRIPTION = "pão de hambúrguer tradicional, 200g de carne, queijo";
    public static final BigDecimal PRICE = new BigDecimal("99.9");
    public static final Integer QUANTITY = 1;
    public static final LocalDateTime TIMESTAMP = LocalDateTime.of(2026, 1,1,
                                                                           12, 0,0);

    public static final ProductRequestDto PRODUCT_REQUEST =
        new ProductRequestDto(VENDOR_ID, PRODUCT_NAME,
                              PRODUCT_DESCRIPTION, PRICE);

    public static final ProductRequestDto INVALID_PRODUCT_REQUEST =
        new ProductRequestDto(null, PRODUCT_NAME,
                              PRODUCT_DESCRIPTION, PRICE);

public static final ProductUpdateDto PRODUCT_UPDATE_REQUEST = new ProductUpdateDto(PRODUCT_ID,
    PRODUCT_DESCRIPTION+"UPDATE", PRICE) ;

    public static final ProductUpdateDto INVALID_PRODUCT_UPDATE_REQUEST = new ProductUpdateDto(null,
                                                   PRODUCT_DESCRIPTION+"UPDATE", PRICE);

    public static ProductResponseDto createValidProductResponse() {
        return new ProductResponseDto(PRODUCT_ID, VENDOR_NAME,
                                      PRODUCT_NAME, PRICE, TIMESTAMP, null);
    }
}
