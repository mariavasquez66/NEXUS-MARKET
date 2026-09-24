package application.domain.valueobjects;

public final class CommercialStatus extends DomainCatalog {
    public static final CommercialStatus ACTIVE = new CommercialStatus("ACTIVE", "Active", "Buyer authorized to make purchases.");
    public static final CommercialStatus RESTRICTED = new CommercialStatus("RESTRICTED", "Restricted", "Buyer with temporary purchasing limitations.");

    private CommercialStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static CommercialStatus fromCode(String code) {
        if ("ACTIVE".equalsIgnoreCase(code)) return ACTIVE;
        if ("RESTRICTED".equalsIgnoreCase(code)) return RESTRICTED;
        throw new IllegalArgumentException("Invalid commercial status code: " + code);
    }
}