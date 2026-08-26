package application.domain.valueobjects;

public final class ShipmentStatus extends DomainCatalog {
    public static final ShipmentStatus PENDING = new ShipmentStatus("PENDING", "Pending", "Shipment not started.");
    public static final ShipmentStatus IN_TRANSIT = new ShipmentStatus("IN_TRANSIT", "In Transit", "Product on the way.");
    public static final ShipmentStatus DELIVERED = new ShipmentStatus("DELIVERED", "Delivered", "Product received by the buyer.");

    private ShipmentStatus(String code, String name, String description) {
        super(code, name, description);
    }
}