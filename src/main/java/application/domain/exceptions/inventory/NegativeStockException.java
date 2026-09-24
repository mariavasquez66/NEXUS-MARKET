package application.domain.exceptions.inventory;

import application.domain.exceptions.common.DomainException;

public class NegativeStockException extends DomainException {
    public NegativeStockException(String message) {
        super(message);
    }

    public NegativeStockException() {
        super("Negative stock balances are not permitted under any circumstances.");
    }
}
