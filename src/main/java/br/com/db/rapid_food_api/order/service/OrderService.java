package br.com.db.rapid_food_api.order.service;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.OrderItem;
import br.com.db.rapid_food_api.order.dto.OrderItemRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.order.dto.OrderResponseDto;
import br.com.db.rapid_food_api.order.mapper.OrderMapper;
import br.com.db.rapid_food_api.order.repository.OrderRepository;
import br.com.db.rapid_food_api.order.service.utils.ProductValidator;
import br.com.db.rapid_food_api.order.service.utils.TotalAmountCalc;
import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final ProductValidator productValidator;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUser(userService.findById(orderRequestDto.userId()));

        for (OrderItemRequestDto itemDto : orderRequestDto.items()) {
            Product product = productValidator.validate(itemDto.productId());
            OrderItem orderItem = new OrderItem(product, itemDto.quantity());
            order.addItem(orderItem);
        }
        order.setTotalAmount(TotalAmountCalc.calculate(order));

        orderRepository.save(order);
        return orderMapper.toDtoResponse(order);
    }
}
