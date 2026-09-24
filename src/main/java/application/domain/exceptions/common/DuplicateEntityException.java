package application.domain.exceptions.common;

public class DuplicateEntityException extends DomainException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
