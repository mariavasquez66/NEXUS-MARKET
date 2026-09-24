package application.domain.exceptions.inventory;

import application.domain.exceptions.common.DomainException;

public class InventoryNotFoundException extends DomainException {
    public InventoryNotFoundException(String productId, String warehouseId) {
        super("Inventory not found for product ID '" + productId + "' in warehouse ID '" + warehouseId + "'.");
    }

    public InventoryNotFoundException(String message) {
        super(message);
    }
}
