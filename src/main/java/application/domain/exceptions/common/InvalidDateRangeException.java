package application.domain.exceptions.common;

public class InvalidDateRangeException extends DomainException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

    public InvalidDateRangeException() {
        super("The provided date range is invalid. Start date must be before or equal to end date.");
    }
}
