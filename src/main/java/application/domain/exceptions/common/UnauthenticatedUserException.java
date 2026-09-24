package application.domain.exceptions.common;

public class UnauthenticatedUserException extends DomainException {
    public UnauthenticatedUserException(String message) {
        super(message);
    }

    public UnauthenticatedUserException() {
        super("Operation requires an authenticated user (RG-01).");
    }
}
