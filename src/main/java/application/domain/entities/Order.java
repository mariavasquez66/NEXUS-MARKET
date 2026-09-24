package application.domain.entities;

import application.domain.valueobjects.OrderStatus;
import application.domain.valueobjects.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Order {
    private String id;
    private Buyer buyer;
    private Map<String, Integer> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(Buyer buyer, Map<String, Integer> items, BigDecimal totalAmount) {
        this.buyer = buyer;
        this.items = items != null ? new HashMap<>(items) : new HashMap<>();
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING_PAYMENT;
        this.paymentStatus = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(String id, Buyer buyer, Map<String, Integer> items, BigDecimal totalAmount) {
        this(buyer, items, totalAmount);
        this.id = id;
    }

    public void confirmPayment() {
        if (!OrderStatus.PENDING_PAYMENT.equals(this.status)) {
            throw new IllegalStateException("Order is not in PENDING_PAYMENT status.");
        }
        this.paymentStatus = PaymentStatus.CONFIRMED;
        this.status = OrderStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public void dispatch() {
        if (!OrderStatus.PAID.equals(this.status)) {
            throw new IllegalStateException("Order must be in PAID status to be dispatched.");
        }
        this.status = OrderStatus.DISPATCHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (!OrderStatus.DISPATCHED.equals(this.status)) {
            throw new IllegalStateException("Order must be in DISPATCHED status to be delivered.");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void finalizeOrder() {
        if (!OrderStatus.DELIVERED.equals(this.status)) {
            throw new IllegalStateException("Order must be in DELIVERED status to be finalized.");
        }
        this.status = OrderStatus.FINALIZED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (isFinalized()) {
            throw new IllegalStateException("A finalized order cannot be cancelled.");
        }
        if (OrderStatus.DELIVERED.equals(this.status)) {
            throw new IllegalStateException("A delivered order cannot be cancelled.");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isFinalized() {
        return OrderStatus.FINALIZED.equals(this.status);
    }

    public boolean isPaid() {
        return OrderStatus.PAID.equals(this.status) || PaymentStatus.CONFIRMED.equals(this.paymentStatus);
    }

    public boolean isCancelled() {
        return OrderStatus.CANCELLED.equals(this.status);
    }

    public boolean isDispatched() {
        return OrderStatus.DISPATCHED.equals(this.status);
    }

    public boolean isDelivered() {
        return OrderStatus.DELIVERED.equals(this.status);
    }
}