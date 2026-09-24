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

    public Return(String id, Order order, String reason) {
        this(order, reason);
        this.id = id;
    }

    public void approve() {
        if (!ReturnStatus.REQUESTED.equals(this.status)) {
            throw new IllegalStateException("Return cannot be approved from status: " + (this.status != null ? this.status.getCode() : "null"));
        }
        this.status = ReturnStatus.APPROVED;
    }

    public void reject() {
        if (!ReturnStatus.REQUESTED.equals(this.status)) {
            throw new IllegalStateException("Return cannot be rejected from status: " + (this.status != null ? this.status.getCode() : "null"));
        }
        this.status = ReturnStatus.REJECTED;
    }

    public void complete() {
        if (!ReturnStatus.APPROVED.equals(this.status)) {
            throw new IllegalStateException("Return must be APPROVED to be completed.");
        }
        this.status = ReturnStatus.COMPLETED;
    }

    public boolean isRequested() {
        return ReturnStatus.REQUESTED.equals(this.status);
    }

    public boolean isApproved() {
        return ReturnStatus.APPROVED.equals(this.status);
    }

    public boolean isRejected() {
        return ReturnStatus.REJECTED.equals(this.status);
    }

    public boolean isCompleted() {
        return ReturnStatus.COMPLETED.equals(this.status);
    }
}
