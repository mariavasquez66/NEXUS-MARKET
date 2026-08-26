package application.domain.valueobjects;

public final class InventoryMovementType extends DomainCatalog {
    public static final InventoryMovementType INGRESS = new InventoryMovementType("INGRESS", "Ingress", "Recording of initial stock or replenishment.");
    public static final InventoryMovementType RESERVATION = new InventoryMovementType("RESERVATION", "Reservation", "Inventory allocation for an order in progress.");
    public static final InventoryMovementType SALE = new InventoryMovementType("SALE", "Sale Outbound", "Dispatch of sold product.");
    public static final InventoryMovementType ADJUSTMENT = new InventoryMovementType("ADJUSTMENT", "Adjustment", "Inventory correction due to shortages or surpluses.");
    public static final InventoryMovementType RETURN = new InventoryMovementType("RETURN", "Return", "Re-entry of product due to a return.");

    private InventoryMovementType(String code, String name, String description) {
        super(code, name, description);
    }
}