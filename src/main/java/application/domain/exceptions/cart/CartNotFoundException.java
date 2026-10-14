package application.domain.exceptions.cart;

import application.domain.exceptions.common.DomainException;

public class CartNotFoundException extends DomainException {

    public CartNotFoundException(String id) {
        super("Shopping cart not found with identifier: " + id);
    }
}
