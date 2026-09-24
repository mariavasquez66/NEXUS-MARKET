package application.domain.exceptions.common;

public class SingleRoleException extends DomainException {
    public SingleRoleException(String message) {
        super(message);
    }

    public SingleRoleException() {
        super("Each user can only have a single role within the system (RG-02).");
    }
}
