package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Invoice {
    private String id;
    private Order order;
    private String invoiceNumber;
    private Double subtotal;
    private Double taxes;
    private Double total;
    private LocalDateTime issuedAt;

    public Invoice(Order order, String invoiceNumber, Double subtotal, Double taxes) {
        this.order = order;
        this.invoiceNumber = invoiceNumber;
        this.subtotal = subtotal;
        this.taxes = taxes;
        this.total = subtotal + taxes;
        this.issuedAt = LocalDateTime.now();
    }
}