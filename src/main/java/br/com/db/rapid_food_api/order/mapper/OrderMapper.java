package br.com.db.rapid_food_api.order.mapper;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.OrderItem;
import br.com.db.rapid_food_api.order.dto.OrderItemRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderRequestDto orderRequestDto);

    OrderResponseDto toDtoResponse(Order order);

    @AfterMapping
    default void setOrderId( @MappingTarget Order order, OrderRequestDto orderRequestDto) {
        if (orderRequestDto.items() != null) {
            List<OrderItem> orderItems = toEntityOrderItems(orderRequestDto.items());
            orderItems.forEach(order::addItem);
        }
    }

    List<OrderItem> toEntityOrderItems(List<OrderItemRequestDto> items);

}
