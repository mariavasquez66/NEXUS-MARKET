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

    public static InventoryMovementType fromCode(String code) {
        if ("INGRESS".equalsIgnoreCase(code)) return INGRESS;
        if ("RESERVATION".equalsIgnoreCase(code)) return RESERVATION;
        if ("SALE".equalsIgnoreCase(code)) return SALE;
        if ("ADJUSTMENT".equalsIgnoreCase(code)) return ADJUSTMENT;
        if ("RETURN".equalsIgnoreCase(code)) return RETURN;
        throw new IllegalArgumentException("Invalid inventory movement type code: " + code);
    }
}