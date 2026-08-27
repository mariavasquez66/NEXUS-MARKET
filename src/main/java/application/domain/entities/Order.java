package application.domain.entities;

import application.domain.valueobjects.OrderStatus;
import application.domain.valueobjects.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Order {
    private String id;
    private Buyer buyer;
    private Map<String, Integer> items;
    private Double totalAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(Buyer buyer, Map<String, Integer> items, Double totalAmount) {
        this.buyer = buyer;
        this.items = new HashMap<>(items);
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING_PAYMENT;
        this.paymentStatus = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void confirmPayment() {
        if (this.status != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("El pedido no está en estado Pendiente de Pago.");
        }
        this.paymentStatus = PaymentStatus.CONFIRMED;
        this.status = OrderStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public void dispatch() {
        if (this.status != OrderStatus.PAID) {
            throw new IllegalStateException("El pedido debe estar Pagado para ser despachado.");
        }
        this.status = OrderStatus.DISPATCHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (this.status != OrderStatus.DISPATCHED) {
            throw new IllegalStateException("El pedido debe estar Despachado para ser entregado.");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void finalize() {
        if (this.status != OrderStatus.DELIVERED) {
            throw new IllegalStateException("El pedido debe estar Entregado para finalizar.");
        }
        this.status = OrderStatus.FINALIZED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isFinalized() {
        return this.status == OrderStatus.FINALIZED;
    }
}