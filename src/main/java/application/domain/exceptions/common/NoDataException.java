package application.domain.exceptions.common;

public class NoDataException extends DomainException {
    public NoDataException(String message) {
        super(message);
    }

    public NoDataException() {
        super("No data available for the requested query or report.");
    }
}
