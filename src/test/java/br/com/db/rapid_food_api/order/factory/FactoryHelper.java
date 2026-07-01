package br.com.db.rapid_food_api.order.factory;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.enums.OrderStatus;
import br.com.db.rapid_food_api.order.dto.*;
import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import br.com.db.rapid_food_api.vendors.domain.Vendor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FactoryHelper {

    public static final UUID DEFAULT_ORDER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID DEFAULT_PRODUCT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    public static final String DEFAULT_PRODUCT_NAME = "Hambúrguer Artesanal";
    public static final BigDecimal DEFAULT_PRICE = new BigDecimal("99.90");
    public static final Integer DEFAULT_QUANTITY = 1;

    public static final OrderRequestDto DEFAULT_ORDER_REQUEST = new OrderRequestDto(
        UserConstants.USER_ID,
        VendorTestFactory.DEFAULT_ID,
        List.of(new OrderItemRequestDto(DEFAULT_PRODUCT_ID, DEFAULT_QUANTITY))
    );

    public static Product createValidProduct() {

        Vendor vendor = VendorTestFactory.createValidVendor();
        Product product = new Product(null, vendor, DEFAULT_PRODUCT_NAME, "Delicioso hambúrguer", DEFAULT_PRICE, LocalDateTime.now(), null);
        product.setId(DEFAULT_PRODUCT_ID);
        return product;
    }

    public static Order createValidOrder() {
        return new Order(
            DEFAULT_ORDER_ID, DEFAULT_PRICE, null, OrderStatus.CREATED,
            LocalDateTime.now(), null, new ArrayList<>(), UserConstants.createUser(),
            VendorTestFactory.createValidVendor()
        );
    }

    public static Order createOrderWithNullId(){
         return new Order(
            null, DEFAULT_PRICE, null, OrderStatus.CREATED,
            LocalDateTime.now(), null, new ArrayList<>(), UserConstants.createUser(),
            VendorTestFactory.createValidVendor()
        );
    }

    public static OrderItemResponseDto createValidItemResponse() {
        return new OrderItemResponseDto(
            DEFAULT_PRODUCT_ID,
            DEFAULT_QUANTITY,
            DEFAULT_PRICE,
            DEFAULT_PRICE,
            DEFAULT_PRODUCT_NAME
        );
    }

    public static OrderResponseDto createValidOrderResponse() {
        return new OrderResponseDto(
            DEFAULT_ORDER_ID, DEFAULT_PRICE, UserConstants.NAME,
            VendorTestFactory.DEFAULT_NAME,
            OrderStatus.CREATED,
            LocalDateTime.now(),
            List.of(createValidItemResponse())
        );
    }

    public static OrderStatusDto createCanceledOrderResponse() {
        return new OrderStatusDto(DEFAULT_ORDER_ID, OrderStatus.CANCELED, "pedido atrasado");
    }

}
