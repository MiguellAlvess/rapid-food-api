package br.com.db.rapid_food_api.payment.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import static br.com.db.rapid_food_api.payment.common.PaymentConstants.CREATE_PAYMENT_REQUEST;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.createPaymentResponse;
import br.com.db.rapid_food_api.payment.dto.PaymentResponse;
import br.com.db.rapid_food_api.payment.service.PaymentService;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void shouldCreatePaymentWithValidData() throws Exception {
        PaymentResponse response = createPaymentResponse();

        when(paymentService.createPayment(CREATE_PAYMENT_REQUEST))
                .thenReturn(response);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CREATE_PAYMENT_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id().toString()))
                .andExpect(jsonPath("$.orderId").value(response.orderId().toString()))
                .andExpect(jsonPath("$.amount").value(response.amount().doubleValue()))
                .andExpect(jsonPath("$.status").value(response.status().name()))
                .andExpect(jsonPath("$.paymentMethod").value(response.paymentMethod().name()));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingPaymentWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "orderId": null,
                          "paymentMethod": null
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
