package application.domain.exceptions.seller;

import application.domain.exceptions.common.DomainException;

public class SelfRegistrationNotAllowedException extends DomainException {
    public SelfRegistrationNotAllowedException() {
        super("Sellers cannot self-register; sellers must be registered by an Administrator.");
    }

    public SelfRegistrationNotAllowedException(String message) {
        super(message);
    }
}
