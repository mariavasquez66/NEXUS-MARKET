package application.domain.valueobjects;

public final class ProductType extends DomainCatalog {
    public static final ProductType PHYSICAL = new ProductType("PHYSICAL", "Physical", "Product requiring inventory and physical shipping.");
    public static final ProductType DIGITAL = new ProductType("DIGITAL", "Digital", "Product delivered immediately after payment confirmation.");

    private ProductType(String code, String name, String description) {
        super(code, name, description);
    }

    public static ProductType fromCode(String code) {
        if ("PHYSICAL".equalsIgnoreCase(code)) return PHYSICAL;
        if ("DIGITAL".equalsIgnoreCase(code)) return DIGITAL;
        throw new IllegalArgumentException("Invalid product type code: " + code);
    }
}