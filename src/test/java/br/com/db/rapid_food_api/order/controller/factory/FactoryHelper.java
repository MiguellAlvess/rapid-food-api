package br.com.db.rapid_food_api.order.controller.factory;

import br.com.db.rapid_food_api.order.dto.OrderItemRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.user.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FactoryHelper {

    public static OrderRequestDto createOrderRequest(User user, UUID productId) {

        return new OrderRequestDto(
                user.getId(), UUID.randomUUID(),
                List.of(orderItemRequestDto(productId))
        );
    }

    public static OrderItemRequestDto orderItemRequestDto(UUID productId){
        return new OrderItemRequestDto(1,productId);
    }

//    ============Products
    public static Product createProduct(){
        return new Product( null, "product", "descrption",
                        new BigDecimal("99.9"), null, null);
    }
}
