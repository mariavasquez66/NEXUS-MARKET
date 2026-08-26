package application.domain.valueobjects;

public final class UserRole extends DomainCatalog {
    public static final UserRole BUYER = new UserRole("BUYER", "Buyer", "Person who purchases published products.");
    public static final UserRole SELLER = new UserRole("SELLER", "Seller", "Responsible for registering and managing their products.");
    public static final UserRole LOGISTICS_OPERATOR = new UserRole("LOGISTICS_OPERATOR", "Logistics Operator", "In charge of the physical operation of warehouses and shipments.");
    public static final UserRole ADMIN = new UserRole("ADMIN", "Administrator", "Responsible for managing sellers and warehouses.");
    public static final UserRole SUPERVISOR = new UserRole("SUPERVISOR", "Supervisor", "Profile for operational consultation and monitoring.");

    private UserRole(String code, String name, String description) {
        super(code, name, description);
    }
}