package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Refund {
    private String id;
    private Return returnRequest;
    private BigDecimal amount;
    private String paymentMethod;
    private boolean processed;

    public Refund(Return returnRequest, BigDecimal amount, String paymentMethod) {
        this.returnRequest = returnRequest;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.processed = false;
    }

    public Refund(String id, Return returnRequest, BigDecimal amount, String paymentMethod) {
        this(returnRequest, amount, paymentMethod);
        this.id = id;
    }

    public void process() {
        if (this.processed) {
            throw new IllegalStateException("Refund is already processed.");
        }
        this.processed = true;
    }

    public boolean isProcessed() {
        return processed;
    }
}
