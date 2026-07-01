package br.com.db.rapid_food_api.payment.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.order.repository.OrderRepository;
import br.com.db.rapid_food_api.payment.domain.Payment;
import br.com.db.rapid_food_api.payment.dto.CreatePaymentRequest;
import br.com.db.rapid_food_api.payment.dto.PaymentResponse;
import br.com.db.rapid_food_api.payment.mapper.PaymentMapper;
import br.com.db.rapid_food_api.payment.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProducer paymentProducer;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + request.orderId() + " not found"));
        Payment payment = new Payment(
                order,
                request.paymentMethod());
        paymentRepository.save(payment);
        paymentProducer.sendPaymentEvent(
                paymentMapper.toEvent(payment));
        return paymentMapper.toResponse(payment);
    }

    @Transactional
    public void confirmPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id " + paymentId + " not found"));
        payment.pay();
        Order order = payment.getOrder();
        order.markAsDelivered();
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

}
