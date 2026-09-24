package application.domain.exceptions.user;

import application.domain.exceptions.common.DomainException;

public class InactiveUserException extends DomainException {
    public InactiveUserException(String message) {
        super(message);
    }

    public InactiveUserException() {
        super("User is currently inactive or blocked and cannot perform operations.");
    }
}
