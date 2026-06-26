package br.com.db.rapid_food_api.order.service;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderResponseDto;
import br.com.db.rapid_food_api.order.mapper.OrderMapper;
import br.com.db.rapid_food_api.order.repository.OrderItemRepository;
import br.com.db.rapid_food_api.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("OrderService client id {}", orderRequestDto.userId() );
        Order order = orderMapper.toEntity(orderRequestDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDtoResponse(savedOrder);
    }
}
