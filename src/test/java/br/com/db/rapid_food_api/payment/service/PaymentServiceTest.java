package br.com.db.rapid_food_api.payment.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.domain.enums.OrderStatus;
import br.com.db.rapid_food_api.order.repository.OrderRepository;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.CREATE_PAYMENT_REQUEST;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.ORDER_ID;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.PAYMENT_ID;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.createOrder;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.createPayment;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.createPaymentEvent;
import static br.com.db.rapid_food_api.payment.common.PaymentConstants.createPaymentResponse;
import br.com.db.rapid_food_api.payment.domain.Payment;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentStatus;
import br.com.db.rapid_food_api.payment.dto.PaymentEvent;
import br.com.db.rapid_food_api.payment.dto.PaymentResponse;
import br.com.db.rapid_food_api.payment.mapper.PaymentMapper;
import br.com.db.rapid_food_api.payment.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentProducer paymentProducer;

    @Test
    void shouldCreatePaymentWhenOrderExists() {
        Order order = createOrder();
        Payment payment = createPayment();
        PaymentEvent event = createPaymentEvent();
        PaymentResponse expectedResponse = createPaymentResponse();

        when(orderRepository.findById(ORDER_ID))
                .thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);
        when(paymentMapper.toEvent(any(Payment.class)))
                .thenReturn(event);
        when(paymentMapper.toResponse(any(Payment.class)))
                .thenReturn(expectedResponse);
        PaymentResponse response = paymentService.createPayment(CREATE_PAYMENT_REQUEST);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(PAYMENT_ID);
        assertThat(response.orderId()).isEqualTo(ORDER_ID);
        assertThat(response.amount()).isEqualByComparingTo(expectedResponse.amount());
        assertThat(response.status()).isEqualTo(PaymentStatus.CREATED);
        assertThat(response.paymentMethod()).isEqualTo(expectedResponse.paymentMethod());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenOrderDoesNotExist() {
        when(orderRepository.findById(ORDER_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.createPayment(CREATE_PAYMENT_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Order with id " + ORDER_ID + " not found");

        verify(paymentRepository, never()).save(any(Payment.class));
        verify(paymentProducer, never()).sendPaymentEvent(any(PaymentEvent.class));
    }

    @Test
    void shouldConfirmPaymentWhenPaymentExists() {
        Payment payment = createPayment();
        Order order = payment.getOrder();

        when(paymentRepository.findById(PAYMENT_ID))
                .thenReturn(Optional.of(payment));

        paymentService.confirmPayment(PAYMENT_ID);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.getUpdatedAt()).isNotNull();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(order.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenPaymentDoesNotExist() {
        when(paymentRepository.findById(PAYMENT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.confirmPayment(PAYMENT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Payment with id " + PAYMENT_ID + " not found");
    }
}
