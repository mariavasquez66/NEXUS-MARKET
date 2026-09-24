package application.domain.valueobjects;

public final class WarehouseType extends DomainCatalog {
    public static final WarehouseType MARKETPLACE = new WarehouseType("MARKETPLACE", "Marketplace", "Warehouse operated directly by the marketplace.");
    public static final WarehouseType SELLER = new WarehouseType("SELLER", "Seller", "Warehouse operated by a registered seller.");

    private WarehouseType(String code, String name, String description) {
        super(code, name, description);
    }

    public static WarehouseType fromCode(String code) {
        if ("MARKETPLACE".equalsIgnoreCase(code)) {
            return MARKETPLACE;
        }
        if ("SELLER".equalsIgnoreCase(code)) {
            return SELLER;
        }
        throw new IllegalArgumentException("Invalid warehouse type code: " + code);
    }
}
