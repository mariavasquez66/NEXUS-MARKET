package application.domain.exceptions.product;

import application.domain.exceptions.common.DomainException;

public class ProductAlreadyPublishedException extends DomainException {
    public ProductAlreadyPublishedException(String id) {
        super("Product is already published: " + id);
    }
}
