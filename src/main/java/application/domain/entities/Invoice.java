package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Invoice {
    private String id;
    private Order order;
    private String invoiceNumber;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal total;
    private LocalDateTime issuedAt;
    private boolean voided;

    public Invoice(Order order, String invoiceNumber, BigDecimal subtotal, BigDecimal taxes) {
        this.order = order;
        this.invoiceNumber = invoiceNumber;
        this.subtotal = subtotal;
        this.taxes = taxes;
        this.total = (subtotal != null && taxes != null) ? subtotal.add(taxes) : BigDecimal.ZERO;
        this.issuedAt = LocalDateTime.now();
        this.voided = false;
    }

    public Invoice(String id, Order order, String invoiceNumber, BigDecimal subtotal, BigDecimal taxes) {
        this(order, invoiceNumber, subtotal, taxes);
        this.id = id;
    }

    public void voidInvoice() {
        if (this.voided) {
            throw new IllegalStateException("Invoice is already voided.");
        }
        this.voided = true;
    }

    public boolean isVoided() {
        return voided;
    }
}