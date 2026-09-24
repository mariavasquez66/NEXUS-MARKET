package application.domain.exceptions.inventory;

import application.domain.exceptions.common.DomainException;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException(String productId, int requested, int available) {
        super("Insufficient stock for product ID '" + productId + "'. Requested: " + requested + ", Available: " + available);
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
