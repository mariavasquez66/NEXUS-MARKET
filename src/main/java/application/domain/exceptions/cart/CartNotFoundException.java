package application.domain.exceptions.cart;

import application.domain.exceptions.common.DomainException;

public class CartNotFoundException extends DomainException {
    public CartNotFoundException(String buyerId) {
        super("Active shopping cart not found for buyer ID: " + buyerId);
    }
}
