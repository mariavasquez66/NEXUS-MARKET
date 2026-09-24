package application.domain.valueobjects;

public final class UserStatus extends DomainCatalog {
    public static final UserStatus ACTIVE = new UserStatus("ACTIVE", "Active", "User enabled to operate on the platform.");
    public static final UserStatus BLOCKED = new UserStatus("BLOCKED", "Blocked", "User temporarily suspended.");
    public static final UserStatus INACTIVE = new UserStatus("INACTIVE", "Inactive", "User registered without recent activity.");

    private UserStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static UserStatus fromCode(String code) {
        if ("ACTIVE".equalsIgnoreCase(code)) return ACTIVE;
        if ("BLOCKED".equalsIgnoreCase(code)) return BLOCKED;
        if ("INACTIVE".equalsIgnoreCase(code)) return INACTIVE;
        throw new IllegalArgumentException("Invalid user status code: " + code);
    }
}