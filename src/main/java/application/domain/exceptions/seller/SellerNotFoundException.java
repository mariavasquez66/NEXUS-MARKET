package application.domain.exceptions.seller;

import application.domain.exceptions.common.DomainException;

public class SellerNotFoundException extends DomainException {
    public SellerNotFoundException(String id) {
        super("Seller not found with identifier: " + id);
    }
}
