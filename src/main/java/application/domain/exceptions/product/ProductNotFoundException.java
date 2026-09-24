package application.domain.exceptions.product;

import application.domain.exceptions.common.DomainException;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String id) {
        super("Product not found with identifier: " + id);
    }
}
