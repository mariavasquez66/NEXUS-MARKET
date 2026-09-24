package application.domain.exceptions.inventory;

import application.domain.exceptions.common.DomainException;

public class DamagedInventoryException extends DomainException {
    public DamagedInventoryException(String message) {
        super(message);
    }

    public DamagedInventoryException(String productId, String warehouseId) {
        super("Cannot reserve damaged inventory for product ID '" + productId + "' in warehouse ID '" + warehouseId + "'.");
    }
}
