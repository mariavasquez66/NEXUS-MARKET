package application.domain.exceptions.buyer;

import application.domain.exceptions.common.DomainException;

public class RestrictedBuyerException extends DomainException {
    public RestrictedBuyerException(String id) {
        super("Buyer commercial status is RESTRICTED: " + id);
    }
}
