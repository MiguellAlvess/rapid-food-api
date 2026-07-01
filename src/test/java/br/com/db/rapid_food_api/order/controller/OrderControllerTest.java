package br.com.db.rapid_food_api.order.controller;

import br.com.db.rapid_food_api.order.factory.FactoryHelper;
import br.com.db.rapid_food_api.order.domain.enums.OrderStatus;
import br.com.db.rapid_food_api.order.dto.*;
import br.com.db.rapid_food_api.order.mapper.OrderMapper;
import br.com.db.rapid_food_api.order.service.OrderService;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderMapper orderMapper;

    @Test
    @DisplayName("Should create an order with valid data")
    void createOrderSuccess() throws Exception {
        OrderRequestDto request = FactoryHelper.DEFAULT_ORDER_REQUEST;
        OrderResponseDto responseMock = FactoryHelper.createValidOrderResponse();

        when(orderService.createOrder(any())).thenReturn(responseMock);

        mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.userName").value(UserConstants.NAME))
               .andExpect(jsonPath("$.vendorName").value(VendorTestFactory.DEFAULT_NAME))
               .andExpect(jsonPath("$.items[0].productName").value(FactoryHelper.DEFAULT_PRODUCT_NAME))
               .andDo(print());
    }

    @Test
    @DisplayName("Should cancel order with valid status")
    void cancelOrder() throws Exception {
        UUID orderId = FactoryHelper.DEFAULT_ORDER_ID;
        var reasonRequestDto = new OrderCancelReasonDto("pedido atrasado");
        OrderStatusDto responseMock = FactoryHelper.createCanceledOrderResponse();

        when(orderService.cancelOrder(eq(orderId), any())).thenReturn(responseMock);

        mockMvc.perform(put("/api/orders/cancel/" + orderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reasonRequestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(OrderStatus.CANCELED.name()))
               .andExpect(jsonPath("$.id").value(orderId.toString()))
               .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar 400 se o ID do usuário for nulo no cadastro")
    void shouldReturn400WhenUserIdIsNull() throws Exception {
        OrderRequestDto invalidRequest = new OrderRequestDto(
            null,
            VendorTestFactory.DEFAULT_ID,
            List.of(new OrderItemRequestDto(FactoryHelper.DEFAULT_PRODUCT_ID, 1))
        );

        mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("Deve retornar 400 se o ID do vendedor for nulo no cadastro")
    void shouldReturn400WhenVendorIdIsNull() throws Exception {
        OrderRequestDto invalidRequest = new OrderRequestDto(
            UserConstants.USER_ID,
            null,
            List.of(new OrderItemRequestDto(FactoryHelper.DEFAULT_PRODUCT_ID, 1))
        );

        mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("Deve retornar 400 se a lista de itens for nula ou vazia")
    void shouldReturn400WhenItemsListIsEmpty() throws Exception {
        OrderRequestDto invalidRequest = new OrderRequestDto(
            UserConstants.USER_ID,
            VendorTestFactory.DEFAULT_ID,
            null
        );

        mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("Deve retornar 400 se a quantidade do produto for nula no item")
    void shouldReturn400WhenItemQuantityIsNull() throws Exception {
        OrderItemRequestDto invalidItem = new OrderItemRequestDto(FactoryHelper.DEFAULT_PRODUCT_ID, null);
        OrderRequestDto invalidRequest = new OrderRequestDto(UserConstants.USER_ID, VendorTestFactory.DEFAULT_ID, List.of(invalidItem));

        mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("Deve retornar 400 se a justificativa de cancelamento estiver em branco")
    void shouldReturn400WhenCancelReasonIsBlank() throws Exception {
        UUID orderId = FactoryHelper.DEFAULT_ORDER_ID;
        var invalidReason = new OrderCancelReasonDto("   ");

        mockMvc.perform(put("/api/orders/cancel/" + orderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidReason)))
               .andExpect(status().isBadRequest());

        verify(orderService, never()).cancelOrder(any(), any());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found se tentar cancelar um pedido inexistente")
    void shouldReturn404WhenOrderToCancelDoesNotExist() throws Exception {
        UUID nonExistentOrderId = UUID.randomUUID();
        var reasonDto = new OrderCancelReasonDto("Pedido não localizado");

        when(orderService.cancelOrder(eq(nonExistentOrderId), any()))
            .thenThrow(new jakarta.persistence.EntityNotFoundException("Pedido não encontrado"));

        mockMvc.perform(put("/api/orders/cancel/" + nonExistentOrderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reasonDto)))
               .andExpect(status().isNotFound())
               .andDo(print());
    }
}
