package application.domain.exceptions.warehouse;

import application.domain.exceptions.common.DomainException;

public class WarehouseNotFoundException extends DomainException {
    public WarehouseNotFoundException(String id) {
        super("Warehouse not found with identifier: " + id);
    }
}
