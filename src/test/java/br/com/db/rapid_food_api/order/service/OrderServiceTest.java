package br.com.db.rapid_food_api.order.service;

import br.com.db.rapid_food_api.order.factory.FactoryHelper;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import org.junit.jupiter.api.Test;
import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.enums.OrderStatus;
import br.com.db.rapid_food_api.order.dto.*;
import br.com.db.rapid_food_api.order.mapper.OrderMapper;
import br.com.db.rapid_food_api.order.repository.OrderRepository;
import br.com.db.rapid_food_api.order.service.utils.ProductValidator;
import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductValidator productValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Deve criar um pedido com sucesso quando os dados forem válidos")
    void createOrderSuccess() {
        OrderRequestDto requestDto = FactoryHelper.DEFAULT_ORDER_REQUEST;
        User user = UserConstants.createUser();
        Vendor vendor = VendorTestFactory.createValidVendor();
        Product product = FactoryHelper.createValidProduct();
        OrderResponseDto expectedResponse = FactoryHelper.createValidOrderResponse();

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.of(user));
        when(productValidator.validate(any(UUID.class))).thenReturn(product);
        when(vendorRepository.findById(requestDto.vendorId())).thenReturn(Optional.of(vendor));
        when(orderMapper.toDtoResponse(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDto actualResponse = orderService.createOrder(requestDto);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.userName(), actualResponse.userName());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException se o usuário não existir na criação")
    void createOrderThrowsWhenUserNotFound() {
        OrderRequestDto requestDto = FactoryHelper.DEFAULT_ORDER_REQUEST;
        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(requestDto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se o produto pertencer a outro vendedor")
    void createOrderThrowsWhenProductVendorDoesNotMatch() {
        OrderRequestDto requestDto = FactoryHelper.DEFAULT_ORDER_REQUEST;
        User user = UserConstants.createUser();

        Product productWithDifferentVendor = FactoryHelper.createValidProduct();
        Vendor differentVendor = new Vendor();
        differentVendor.setId(UUID.randomUUID());
        productWithDifferentVendor.setVendor(differentVendor);

        when(userRepository.findById(requestDto.userId())).thenReturn(Optional.of(user));
        when(productValidator.validate(any(UUID.class))).
                                                            thenReturn(productWithDifferentVendor);

        assertThrows(IllegalArgumentException.class, () ->
            orderService.createOrder(requestDto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar o status do pedido corretamente pelo ID")
    void viewOrderStatusSuccess() {
        UUID orderId = FactoryHelper.DEFAULT_ORDER_ID;
        Order order = FactoryHelper.createValidOrder();
        OrderStatusDto expectedStatusDto = new OrderStatusDto(orderId,
                                                              OrderStatus.CREATED, null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderStatus(order)).thenReturn(expectedStatusDto);

        OrderStatusDto actualStatusDto = orderService.viewOrderStatus(orderId);

        assertNotNull(actualStatusDto);
        assertEquals(OrderStatus.CREATED, actualStatusDto.status());
    }

    @Test
    @DisplayName("Deve cancelar o pedido com sucesso se o status permitir")
    void cancelOrderSuccess() {
        UUID orderId = FactoryHelper.DEFAULT_ORDER_ID;
        OrderCancelReasonDto reasonDto = new OrderCancelReasonDto("Atrasou muito");
        Order order = FactoryHelper.createValidOrder();

        OrderStatusDto expectedStatusDto = new OrderStatusDto(orderId, OrderStatus.CANCELED,
                                                              "Atrasou muito");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderStatus(order)).thenReturn(expectedStatusDto);

        OrderStatusDto actualStatusDto = orderService.cancelOrder(orderId, reasonDto);

        assertNotNull(actualStatusDto);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        assertEquals("Atrasou muito", order.getObservation());
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar cancelar se cantBeCancelled for verdadeiro")
    void cancelOrderThrowsWhenCantBeCancelled() {
        UUID orderId = FactoryHelper.DEFAULT_ORDER_ID;
        OrderCancelReasonDto reasonDto = new OrderCancelReasonDto("Mudei de ideia");
        Order order = FactoryHelper.createValidOrder();

        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () ->
            orderService.cancelOrder(orderId, reasonDto));
    }

    @Test
    @DisplayName("Deve retornar uma página de pedidos DTO para um usuário")
    void getOrdersPageSuccess() {
        UUID userId = UserConstants.USER_ID;
        Pageable pageable = PageRequest.of(0, 10);

        Order order = FactoryHelper.createValidOrder();
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        OrderResponseDto responseDto = FactoryHelper.createValidOrderResponse();

        when(orderRepository.findAllByUserOrderedDesc(userId, pageable)).thenReturn(orderPage);
        when(orderMapper.toDtoResponse(order)).thenReturn(responseDto);

        Page<OrderResponseDto> actualPage = orderService.getOrders(userId, pageable);

        assertNotNull(actualPage);
        assertEquals(1, actualPage.getTotalElements());
        assertEquals(responseDto.userName(), actualPage.getContent().get(0).userName());
    }
}
