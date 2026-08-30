package application.domain.entities;

import application.domain.valueobjects.ReturnStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Return {
    private String id;
    private Order order;
    private String reason;
    private ReturnStatus status;

    public Return(Order order, String reason) {
        this.order = order;
        this.reason = reason;
        this.status = ReturnStatus.REQUESTED;
    }

    public void approve() {
        this.status = ReturnStatus.APPROVED;
    }

    public void reject() {
        this.status = ReturnStatus.REJECTED;
    }

    public void complete() {
        this.status = ReturnStatus.COMPLETED;
    }
}
