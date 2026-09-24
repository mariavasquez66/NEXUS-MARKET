package application.domain.exceptions.common;

public class InsufficientPermissionException extends DomainException {
    public InsufficientPermissionException(String message) {
        super(message);
    }

    public InsufficientPermissionException() {
        super("User does not have sufficient permissions to perform this operation (RG-03).");
    }
}
