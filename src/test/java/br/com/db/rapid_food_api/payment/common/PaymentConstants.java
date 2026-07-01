package br.com.db.rapid_food_api.payment.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.payment.domain.Payment;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentMethod;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentStatus;
import br.com.db.rapid_food_api.payment.dto.CreatePaymentRequest;
import br.com.db.rapid_food_api.payment.dto.PaymentEvent;
import br.com.db.rapid_food_api.payment.dto.PaymentResponse;

public class PaymentConstants {

    public static final UUID ORDER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID PAYMENT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    public static final BigDecimal AMOUNT = BigDecimal.valueOf(100.00);

    public static final PaymentMethod PAYMENT_METHOD = PaymentMethod.PIX;

    public static final CreatePaymentRequest CREATE_PAYMENT_REQUEST = new CreatePaymentRequest(ORDER_ID,
            PAYMENT_METHOD);

    public static Order createOrder() {
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setTotalAmount(AMOUNT);
        return order;
    }

    public static Payment createPayment() {
        Payment payment = new Payment(createOrder(), PAYMENT_METHOD);
        payment.setId(PAYMENT_ID);
        return payment;
    }

    public static PaymentEvent createPaymentEvent() {
        return new PaymentEvent(
                PAYMENT_ID,
                ORDER_ID,
                AMOUNT,
                PAYMENT_METHOD);
    }

    public static PaymentResponse createPaymentResponse() {
        return new PaymentResponse(
                PAYMENT_ID,
                ORDER_ID,
                AMOUNT,
                PaymentStatus.CREATED,
                PAYMENT_METHOD,
                LocalDateTime.now(),
                null);
    }

    private PaymentConstants() {
    }
}
