package application.domain.valueobjects;

public final class UserRole extends DomainCatalog {
    UserRole BUYER = new UserRole("BUYER", "Person who acquires purchased products.");
    UserRole SELLER = new UserRole("SELLER", "Responsible for registering and managing your products.");
    UserRole LOGISTICS_OPERATOR = new UserRole("LOGISTICS_OPERATOR", "Responsible for the physical operations of werehouses and shopping facilities.");
    UserRole ADMIN = new UserRole("ADMIN", "Responsible for the administration of vendors and werehouses.");
    UserRole SUPERVISOR = new UserRole("SUPERVISOR", "Consultation and operational monitoring profile.")

    private UserRole(String code, String name, String description) {
        super(code, name, description);
    }
}