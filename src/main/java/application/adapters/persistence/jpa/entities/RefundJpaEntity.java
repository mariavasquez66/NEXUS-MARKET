package application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "refunds")
public class RefundJpaEntity {

    @Id
    @Column(length = 64)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false, unique = true)
    private ReturnJpaEntity returnRequest;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 100)
    private String paymentMethod;

    @Column(nullable = false)
    private boolean processed;

    public RefundJpaEntity() {
    }

    public RefundJpaEntity(String id, ReturnJpaEntity returnRequest, BigDecimal amount, String paymentMethod, boolean processed) {
        this.id = id;
        this.returnRequest = returnRequest;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.processed = processed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReturnJpaEntity getReturnRequest() {
        return returnRequest;
    }

    public void setReturnRequest(ReturnJpaEntity returnRequest) {
        this.returnRequest = returnRequest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
