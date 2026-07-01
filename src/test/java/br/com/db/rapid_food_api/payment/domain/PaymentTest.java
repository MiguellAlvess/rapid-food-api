package br.com.db.rapid_food_api.payment.domain;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentMethod;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentStatus;

class PaymentTest {

    @Test
    void shouldCreatePaymentWithCreatedStatus() {
        Order order = new Order();
        order.setTotalAmount(BigDecimal.valueOf(100.00));

        Payment payment = new Payment(order, PaymentMethod.PIX);

        assertThat(payment.getOrder()).isEqualTo(order);
        assertThat(payment.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(payment.getPaymentMethod()).isEqualTo(PaymentMethod.PIX);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CREATED);
        assertThat(payment.getCreatedAt()).isNotNull();
        assertThat(payment.getUpdatedAt()).isNull();
    }

    @Test
    void shouldAuthorizePayment() {
        Payment payment = new Payment();
        payment.authorize();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.AUTHORIZED);
        assertThat(payment.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldPayPayment() {
        Payment payment = new Payment();
        payment.pay();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldRefundPayment() {
        Payment payment = new Payment();
        payment.refund();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(payment.getUpdatedAt()).isNotNull();
    }
}
