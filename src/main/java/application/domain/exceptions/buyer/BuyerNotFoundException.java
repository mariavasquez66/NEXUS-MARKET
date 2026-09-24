package application.domain.exceptions.buyer;

import application.domain.exceptions.common.DomainException;

public class BuyerNotFoundException extends DomainException {
    public BuyerNotFoundException(String id) {
        super("Buyer not found with identifier: " + id);
    }
}
