package application.domain.exceptions.common;

public class InvalidDataException extends DomainException {
    public InvalidDataException(String message) {
        super(message);
    }
}
