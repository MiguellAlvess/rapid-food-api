package br.com.db.rapid_food_api.order.domain.enums;

public enum OrderStatus {
    CREATED,
    SENT_TO_VENDOR,
    ACCEPTED,
    PREPARING,
    DELIVERED,
    CANCELED
}
