package application.domain.exceptions.user;

import application.domain.exceptions.common.DomainException;

public class DuplicateEmailException extends DomainException {
    public DuplicateEmailException(String email) {
        super("Email is already registered: " + email);
    }
}
