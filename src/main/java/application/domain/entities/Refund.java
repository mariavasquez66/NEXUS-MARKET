package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Refund {
    private String id;
    private Return returnRequest;
    private Double amount;
    private String paymentMethod;

    public Refund(Return returnRequest, Double amount, String paymentMethod) {
        this.returnRequest = returnRequest;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }