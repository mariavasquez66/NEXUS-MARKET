package application.domain.exceptions.user;

import application.domain.exceptions.common.DomainException;

public class DuplicateIdentificationException extends DomainException {
    public DuplicateIdentificationException(String id) {
        super("User identification document is already registered: " + id);
    }
}
