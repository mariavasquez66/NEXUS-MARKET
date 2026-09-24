package application.domain.valueobjects;

public final class ProductStatus extends DomainCatalog {
    public static final ProductStatus PUBLISHED = new ProductStatus("PUBLISHED", "Published", "Product is visible in the public catalog and available for purchase.");
    public static final ProductStatus SUSPENDED = new ProductStatus("SUSPENDED", "Suspended", "Product is temporarily hidden from the public catalog.");
    public static final ProductStatus DISCONTINUED = new ProductStatus("DISCONTINUED", "Discontinued", "Product is permanently retired from the catalog.");

    private ProductStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static ProductStatus fromCode(String code) {
        if ("PUBLISHED".equalsIgnoreCase(code)) {
            return PUBLISHED;
        }
        if ("SUSPENDED".equalsIgnoreCase(code)) {
            return SUSPENDED;
        }
        if ("DISCONTINUED".equalsIgnoreCase(code)) {
            return DISCONTINUED;
        }
        throw new IllegalArgumentException("Invalid product status code: " + code);
    }
}
