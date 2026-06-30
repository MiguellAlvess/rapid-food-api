package br.com.db.rapid_food_api.payment.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import br.com.db.rapid_food_api.payment.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "payment-topic", groupId = "rapid-food-group")
    public void consume(PaymentEvent event) {
        log.info("Payment event received. paymentId={}, orderId={}",
                event.paymentId(),
                event.orderId());
        paymentService.confirmPayment(event.paymentId());
    }
}
