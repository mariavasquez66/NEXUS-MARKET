package application.domain.exceptions.product;

import application.domain.exceptions.common.DomainException;

public class ProductNotPublishedException extends DomainException {
    public ProductNotPublishedException(String id) {
        super("Product is not published in the catalog: " + id);
    }
}
