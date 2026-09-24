package application.domain.exceptions.seller;

import application.domain.exceptions.common.DomainException;

public class SellerWithoutWarehouseException extends DomainException {
    public SellerWithoutWarehouseException(String sellerId) {
        super("Seller has no associated warehouses: " + sellerId);
    }
}
