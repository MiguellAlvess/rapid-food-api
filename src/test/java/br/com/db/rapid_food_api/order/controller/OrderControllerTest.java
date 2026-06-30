package br.com.db.rapid_food_api.order.controller;

import br.com.db.rapid_food_api.order.controller.factory.FactoryHelper;
import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.enums.OrderStatus;
import br.com.db.rapid_food_api.order.dto.OrderCancelReasonDto;
import br.com.db.rapid_food_api.order.dto.OrderRequestDto;
import br.com.db.rapid_food_api.order.repository.OrderRepository;
import br.com.db.rapid_food_api.product.domain.Product;
import br.com.db.rapid_food_api.product.repository.ProductRepository;
import br.com.db.rapid_food_api.user.common.UserConstants;
import br.com.db.rapid_food_api.user.domain.User;
import br.com.db.rapid_food_api.user.repository.UserRepository;
import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<OrderRequestDto> oderRequestDtoJson;

    @Autowired
    private JacksonTester<OrderCancelReasonDto> cancelReasonDtoJson;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create an order with valid data")
    void createOrder() throws Exception {
        User user = userRepository.save(UserConstants.createUser());
        Vendor vendor = vendorRepository.save(FactoryHelper.createVendor());
        Product product = productRepository.save(FactoryHelper.createProduct(vendor));
        OrderRequestDto orderRequestDto = FactoryHelper.createOrderRequest(user, product.getId(), vendor);

        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                                           .content(oderRequestDtoJson.write(orderRequestDto).getJson()))
               .andExpect(status().isCreated()).andExpect(jsonPath("$.userName").value(user.getName()))
               .andExpect(jsonPath("$.items[0].productName").value(product.getProductName())).andDo(print());
        Assertions.assertEquals(1, orderRepository.count());
    }

    @Test
    @DisplayName("Should cancel order with valid status")
    void cancelOrder() throws Exception {
        User user = userRepository.save(UserConstants.createUser());
        Vendor vendor = vendorRepository.save(FactoryHelper.createVendor());
        Order order = orderRepository.save(FactoryHelper.createOrder(user, vendor));

        var reasonRequestDto = new OrderCancelReasonDto("pedido atrasado");

        mockMvc.perform(put("/api/orders/cancel/" + order.getId()).contentType(MediaType.APPLICATION_JSON).content(
                   cancelReasonDtoJson.write(reasonRequestDto).getJson())).andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value(OrderStatus.CANCELED.name()))
               .andExpect(jsonPath("$.id").value(order.getId().toString())).andDo(print());
    }

    @Test
    @DisplayName("Should not cancel order with status PREPARING")
    void shouldNotCancelOrderPREPARING() throws Exception {
        User user = userRepository.save(UserConstants.createUser());
        Vendor vendor = vendorRepository.save(FactoryHelper.createVendor());
        Order order = orderRepository.save(FactoryHelper.createOrder(user, vendor));

        order.setStatus(OrderStatus.PREPARING);
        orderRepository.save(order);

        var reasonRequestDto = new OrderCancelReasonDto("pedido atrasado");

        mockMvc.perform(put("/api/orders/cancel/" + order.getId()).contentType(MediaType.APPLICATION_JSON).content(
            cancelReasonDtoJson.write(reasonRequestDto).getJson())).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("Should not cancel order with status DELIVERED")
    void shouldNotCancelOrderDELIVERED() throws Exception {
        User user = userRepository.save(UserConstants.createUser());
        Vendor vendor = vendorRepository.save(FactoryHelper.createVendor());
        Order order = orderRepository.save(FactoryHelper.createOrder(user, vendor));

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        var reasonRequestDto = new OrderCancelReasonDto("pedido atrasado");

        mockMvc.perform(put("/api/orders/cancel/" + order.getId()).contentType(MediaType.APPLICATION_JSON).content(
            cancelReasonDtoJson.write(reasonRequestDto).getJson())).andExpect(status().isBadRequest()).andDo(print());
    }

}
