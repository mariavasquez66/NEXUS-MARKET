package application.domain.valueobjects;

public final class ReturnStatus extends DomainCatalog {
    public static final ReturnStatus REQUESTED = new ReturnStatus("REQUESTED", "Requested", "Return initiated by the buyer.");
    public static final ReturnStatus APPROVED = new ReturnStatus("APPROVED", "Approved", "Return accepted.");
    public static final ReturnStatus REJECTED = new ReturnStatus("REJECTED", "Rejected", "Return denied.");
    public static final ReturnStatus COMPLETED = new ReturnStatus("COMPLETED", "Completed", "Return finalized.");

    private ReturnStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static ReturnStatus fromCode(String code) {
        if ("REQUESTED".equalsIgnoreCase(code)) return REQUESTED;
        if ("APPROVED".equalsIgnoreCase(code)) return APPROVED;
        if ("REJECTED".equalsIgnoreCase(code)) return REJECTED;
        if ("COMPLETED".equalsIgnoreCase(code)) return COMPLETED;
        throw new IllegalArgumentException("Invalid return status code: " + code);
    }
}