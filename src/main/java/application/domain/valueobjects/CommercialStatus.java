package application.domain.valueobjects;

public final class CommercialStatus extends DomainCatalog {
    public static final CommercialStatus ACTIVE = new CommercialStatus("ACTIVE", "Active", "Buyer authorized to make purchases.");
    public static final CommercialStatus RESTRICTED = new CommercialStatus("RESTRICTED", "Restricted", "Buyer with temporary purchasing limitations.");

    private CommercialStatus(String code, String name, String description) {
        super(code, name, description);
    }
}