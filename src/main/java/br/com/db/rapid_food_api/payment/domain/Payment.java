package br.com.db.rapid_food_api.payment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.db.rapid_food_api.order.domain.Order;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentMethod;
import br.com.db.rapid_food_api.payment.domain.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Payment() {
        this.status = PaymentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public Payment(Order order, PaymentMethod paymentMethod) {
        this.order = order;
        this.amount = order.getTotalAmount();
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public void authorize() {
        this.status = PaymentStatus.AUTHORIZED;
        this.updatedAt = LocalDateTime.now();
    }

    public void pay() {
        this.status = PaymentStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }
}
