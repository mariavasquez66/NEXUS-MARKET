package application.domain.exceptions.inventory;

import application.domain.exceptions.common.DomainException;

public class InvalidAdjustmentException extends DomainException {
    public InvalidAdjustmentException(String message) {
        super(message);
    }
}
