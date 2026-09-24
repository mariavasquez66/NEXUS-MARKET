package application.domain.exceptions.user;

import application.domain.exceptions.common.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String id) {
        super("User not found with identifier: " + id);
    }
}
