package application.domain.valueobjects;

public final class UserStatus extends DomainCatalog {
    public static final UserStatus ACTIVE = new UserStatus("ACTIVE", "Active", "User enabled to operate on the platform.");
    public static final UserStatus BLOCKED = new UserStatus("BLOCKED", "Blocked", "User temporarily suspended.");
    public static final UserStatus INACTIVE = new UserStatus("INACTIVE", "Inactive", "User registered without recent activity.");

    private UserStatus(String code, String name, String description) {
        super(code, name, description);
    }
}