package br.com.db.rapid_food_api.order.controller.factory;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.enums.OrderStatus;
import br.com.db.rapid_food_api.order.dto.OrderItemRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.vendors.domain.Vendor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FactoryHelper {

    public static Order createOrder(User user, Vendor vendor) {
        return new Order(null, new BigDecimal("99.99"), null, OrderStatus.CREATED, LocalDateTime.now(), null,
                         new ArrayList<>(), user, vendor);

    }

    public static OrderRequestDto createOrderRequest(User user, UUID productId, Vendor vendor) {
        return new OrderRequestDto(user.getId(), vendor.getId(), List.of(orderItemRequestDto(productId)));
    }

    public static OrderItemRequestDto orderItemRequestDto(UUID productId) {
        return new OrderItemRequestDto(productId, 1);
    }

    //    ============Products
    public static Product createProduct(Vendor vendor) {
        return new Product(null, vendor, "product", "descrption", new BigDecimal("99.9"), LocalDateTime.now(), null);
    }

    // ============= Vendor

    public static Vendor createVendor() {
        Vendor vendor = new Vendor();
        vendor.setActive(true);
        vendor.setName("Vendor");
        vendor.setCnpj("11258699000100");
        return vendor;
    }
}
