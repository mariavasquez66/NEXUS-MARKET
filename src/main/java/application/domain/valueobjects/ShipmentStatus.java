package application.domain.valueobjects;

public final class ShipmentStatus extends DomainCatalog {
    public static final ShipmentStatus PENDING = new ShipmentStatus("PENDING", "Pending", "Shipment not started.");
    public static final ShipmentStatus IN_TRANSIT = new ShipmentStatus("IN_TRANSIT", "In Transit", "Product on the way.");
    public static final ShipmentStatus DELIVERED = new ShipmentStatus("DELIVERED", "Delivered", "Product received by the buyer.");

    private ShipmentStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static ShipmentStatus fromCode(String code) {
        if ("PENDING".equalsIgnoreCase(code)) return PENDING;
        if ("IN_TRANSIT".equalsIgnoreCase(code)) return IN_TRANSIT;
        if ("DELIVERED".equalsIgnoreCase(code)) return DELIVERED;
        throw new IllegalArgumentException("Invalid shipment status code: " + code);
    }
}