package application.domain.exceptions.product;

import application.domain.exceptions.common.DomainException;

import java.math.BigDecimal;

public class InvalidPriceException extends DomainException {
    public InvalidPriceException(BigDecimal price) {
        super("Invalid product price: " + price + ". Price must be greater than zero.");
    }

    public InvalidPriceException(String message) {
        super(message);
    }
}
